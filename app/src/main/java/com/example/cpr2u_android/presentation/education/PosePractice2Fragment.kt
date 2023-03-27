package com.example.cpr2u_android.presentation.education

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Point
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.FragmentPosePractice2Binding
import com.example.cpr2u_android.ml.camera.CameraSource
import com.example.cpr2u_android.ml.data.BodyPart
import com.example.cpr2u_android.ml.data.Device
import com.example.cpr2u_android.ml.data.Person
import com.example.cpr2u_android.ml.ml.ModelType
import com.example.cpr2u_android.ml.ml.MoveNet
import com.example.cpr2u_android.presentation.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class PosePractice2Fragment :
    BaseFragment<FragmentPosePractice2Binding>(R.layout.fragment_pose_practice_2) {

    companion object {
        private const val FRAGMENT_DIALOG = "dialog"
    }

    private lateinit var surfaceView: SurfaceView
    private var modelPos = 1 // 1 == MoveNet Thunder model
    private var device = Device.GPU
    private lateinit var tvScore: TextView
    private var cameraSource: CameraSource? = null

    /**
     * CPR 자세 인식에 필요한 변수들
     */
    private var maxHeight = 0f
    private var minHeight = 0f
    private var beforeWrist = 0f
    private var increased = true
    private var wristList = arrayListOf<Float>()

    private val TAG = "CPR2U"

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your app.
                openCamera()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
//                ErrorDialog.newInstance(getString(R.string.tfe_pe_request_permission))
//                    .show(supportFragmentManager, FRAGMENT_DIALOG)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val camera = Camera.CameraInfo.CAMERA_FACING_FRONT
        // keep screen on while app is running
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        tvScore = view.findViewById(R.id.tvScore)
        surfaceView = view.findViewById(R.id.surfaceView)
        surfaceView.display
        if (!isCameraPermissionGranted()) {
            requestPermission()
        }
    }

    override fun onStart() {
        super.onStart()
        openCamera()
    }

    override fun onResume() {
        cameraSource?.resume()
        super.onResume()
    }

    override fun onPause() {
        cameraSource?.close()
        cameraSource = null
        super.onPause()
    }

    // check if permission is granted or not.
    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA,
        ) == PackageManager.PERMISSION_GRANTED
    }

    // open camera
    private fun openCamera() {
        val display = activity?.windowManager?.defaultDisplay // in case of Activity
/* val display = activity!!.windowManaver.defaultDisplay */ // in case of Fragment
        if (isCameraPermissionGranted()) {
            if (cameraSource == null) {
                cameraSource =
                    CameraSource(
                        surfaceView,
                        object : CameraSource.CameraSourceListener {
                            override fun onFPSListener(fps: Int) {
//                                tvFPS.text = getString(R.string.tfe_pe_tv_fps, fps)
                            }

                            override fun onDetectedInfo(
                                personScore: Float?,
                                poseLabels: List<Pair<String, Float>>?,
                                persons: List<Person>,
                            ) {
                                // tvScore: 자세 인식 모델의 정확도 점수
                                tvScore.text =
                                    getString(R.string.tfe_pe_tv_score, personScore ?: 0f)
                                Timber.d("TV SCORE -> $personScore")

                                /**
                                 * TODO: 여기서부터 CPR 자세 인식 코드 시작
                                 * persons에 더 많은 person 데이터가 있을 수록 정확도가 높아진다.
                                 * persons의 0번째에 있는 데이터를 가져와 자세를 분석한다.
                                 */
                                measureCprScore(persons[0])
                            }
                        },
                    ).apply {
                        prepareCamera()
                    }
                lifecycleScope.launch(Dispatchers.Main) {
                    cameraSource?.initCamera()
                }
            }
            createPoseEstimator()
        }
    }

    /**
     * CPR 자세 인식
     */
    private fun measureCprScore(person: Person) {
        var xShoulder = .0f
        var yShoulder = .0f
        var xElbow = .0f
        var yElbow = .0f
        var xWrist = .0f
        var yWrist = .0f

        // person이 갖고 있는 관절 데이터들에서 어깨, 팔꿈치, 손목 데이터 추출 (현재 임시로 왼쪽 관절만 추출한 상태)
        person.keyPoints.forEach { point ->
            when (point.bodyPart) {
                BodyPart.LEFT_SHOULDER -> {
                    xShoulder = point.coordinate.x
                    yShoulder = point.coordinate.y
                }
                BodyPart.LEFT_ELBOW -> {
                    xElbow = point.coordinate.x
                    yElbow = point.coordinate.y
                }
                BodyPart.LEFT_WRIST -> {
                    xWrist = point.coordinate.x
                    yWrist = point.coordinate.y
                }
                else -> {}
            }
        }

        // 어깨, 팔꿈치, 손목이 일직선인지 x 값으로 확인한다. (약간의 노이즈 발생으로 인해 10의 여유를 둠)
        var isCorrect = xShoulder - xElbow < 10 && xElbow - xWrist < 10
        if (isCorrect) {
            Log.i(TAG, "올바른 자세에요!")
        } else {
            Log.i(TAG, "팔을 90도로 유지하세요!")
        }

        // 손목의 높이가 상승 곡선에서 꼭짓점을 찍고 하강하는 경우
        if (increased && beforeWrist > yWrist + 1) {
            increased = false
            maxHeight = yWrist
        }
        // 손목의 높이가 하강 곡선에서 꼭짓점을 찍고 상승하는 경우
        else if (!increased && beforeWrist < yWrist - 1) {
            increased = true
            minHeight = yWrist

            // wristList에 ${손목의 최대 높이 - 손목의 최소 높이}를 저장
            wristList.add(maxHeight - minHeight)
            Log.e(TAG, "wristList : $wristList")
            // wristList에 저장된 깊이 값으로 CPR 깊이가 적절한지 확인한다.
            // wristList에 저장된 값의 개수로 CPR 속도(2분 동안 CPR한 횟수)가 적절한지 확인한다.
            //  가슴압박 속도는 분당 100~120회, 깊이는 5~6㎝로 빠르고 깊게 30회 압박
            // 2분 -> 200~240회 : 추후 1분당 평균 내는것도 나쁘지 않을듯

            Log.i(TAG, "현재 손목 깊이: ${maxHeight - minHeight}, max: $maxHeight, min: $minHeight")
        }

        if (increased) {
            Log.i("CPR2U", "손목이 상승 중입니다.")
        } else {
            Log.i(TAG, "손목이 하강 중입니다.")
        }

        beforeWrist = yWrist
    }

    // 자세 추정 모델 실행 (Movenet Thunder, CPU가 적절)
    private fun createPoseEstimator() {
        // For MoveNet MultiPose, hide score and disable pose classifier as the model returns
        // multiple Person instances.
        val poseDetector = when (modelPos) {
            1 -> {
                // MoveNet Thunder (SinglePose)
                showDetectionScore(true)
                MoveNet.create(requireContext(), device, ModelType.Thunder)
            }
            else -> {
                null
            }
        }
        poseDetector?.let { detector ->
            cameraSource?.setDetector(detector)
        }
    }

    // Show/hide the detection score.
    private fun showDetectionScore(isVisible: Boolean) {
        tvScore.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun requestPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA,
            ),
            -> {
                // You can use the API that requires the permission.
                openCamera()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA,
                )
            }
        }
    }

    /**
     * Shows an error message dialog.
     */
    class ErrorDialog : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                .setMessage(requireArguments().getString(ARG_MESSAGE))
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    // do nothing
                }
                .create()

        companion object {

            @JvmStatic
            private val ARG_MESSAGE = "message"

            @JvmStatic
            fun newInstance(message: String): ErrorDialog = ErrorDialog().apply {
                arguments = Bundle().apply { putString(ARG_MESSAGE, message) }
            }
        }
    }
}
