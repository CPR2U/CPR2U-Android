package com.example.cpr2u_android.presentation.education

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.FragmentPosePractice2Binding
import com.example.cpr2u_android.ml.camera.CameraSource
import com.example.cpr2u_android.ml.data.BodyPart
import com.example.cpr2u_android.ml.data.Device
import com.example.cpr2u_android.ml.data.Person
import com.example.cpr2u_android.ml.ml.*
import com.example.cpr2u_android.presentation.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class PosePractice2Fragment :
    BaseFragment<FragmentPosePractice2Binding>(R.layout.fragment_pose_practice_2) {

    companion object {
        private const val FRAGMENT_DIALOG = "dialog"
    }

    /** A [SurfaceView] for camera preview.   */
    private lateinit var surfaceView: SurfaceView

    /** Default pose estimation model is 1 (MoveNet Thunder)
     * 0 == MoveNet Lightning model
     * 1 == MoveNet Thunder model
     * 2 == MoveNet MultiPose model
     * 3 == PoseNet model
     *
     * => 1(Movenet Thunder)를 사용할 것. 제일 정확도가 높음.
     **/
    private var modelPos = 1

    /** Default device is CPU */
    private var device = Device.CPU

    private lateinit var tvScore: TextView
    private lateinit var tvFPS: TextView
    private lateinit var spnDevice: Spinner
    private lateinit var spnModel: Spinner
    private lateinit var spnTracker: Spinner
    private lateinit var vTrackerOption: View
    private lateinit var tvClassificationValue1: TextView
    private lateinit var tvClassificationValue2: TextView
    private lateinit var tvClassificationValue3: TextView
    private lateinit var swClassification: SwitchCompat
    private lateinit var vClassificationOption: View
    private var cameraSource: CameraSource? = null
    private var isClassifyPose = false

    /**
     * CPR 자세 인식에 필요한 변수들
     */
    private var maxHeight = 0f
    private var minHeight = 0f
    private var beforeWrist = 0f
    private var increased = true
    private var wristList = arrayListOf<Float>()

    private final val TAG = "CPR2U"

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
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
    private var changeModelListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            // do nothing
        }

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long,
        ) {
            changeModel(position)
        }
    }

    private var changeDeviceListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            changeDevice(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // do nothing
        }
    }

    private var changeTrackerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            changeTracker(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // do nothing
        }
    }

    private var setClassificationListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->
            showClassificationResult(isChecked)
            isClassifyPose = isChecked
            isPoseClassifier()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("에엥.. 여기까지 오긴하니")
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // keep screen on while app is running
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        tvScore = view.findViewById(R.id.tvScore)
        tvFPS = view.findViewById(R.id.tvFps)
        spnModel = view.findViewById(R.id.spnModel)
        spnDevice = view.findViewById(R.id.spnDevice)
        spnTracker = view.findViewById(R.id.spnTracker)
        vTrackerOption = view.findViewById(R.id.vTrackerOption)
        surfaceView = view.findViewById(R.id.surfaceView)
        tvClassificationValue1 = view.findViewById(R.id.tvClassificationValue1)
        tvClassificationValue2 = view.findViewById(R.id.tvClassificationValue2)
        tvClassificationValue3 = view.findViewById(R.id.tvClassificationValue3)
        swClassification = view.findViewById(R.id.swPoseClassification)
        vClassificationOption = view.findViewById(R.id.vClassificationOption)
        initSpinner()
        spnModel.setSelection(modelPos)
        swClassification.setOnCheckedChangeListener(setClassificationListener)
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

                                // tvClassificationValue1~3: 내가 선택한 ML 모델 옵션(CPU/GPU, 모델 종류 등)
                                poseLabels?.sortedByDescending { it.second }?.let {
                                    tvClassificationValue1.text = "에엥..."
                                    tvClassificationValue2.text = "에에엥..."
                                    tvClassificationValue3.text = "에에에엥..."
                                }

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
                isPoseClassifier()
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

            // wristList에 저장된 깊이 값으로 CPR 깊이가 적절한지 확인한다.
            // wristList에 저장된 값의 개수로 CPR 속도(2분 동안 CPR한 횟수)가 적절한지 확인한다.
            Log.i(TAG, "현재 손목 깊이: ${maxHeight - minHeight}, max: $maxHeight, min: $minHeight")
        }

        if (increased) {
            Log.i("CPR2U", "손목이 상승 중입니다.")
        } else {
            Log.i(TAG, "손목이 하강 중입니다.")
        }

        beforeWrist = yWrist
    }

    private fun convertPoseLabels(pair: Pair<String, Float>?): String {
        if (pair == null) return "empty"
        return "${pair.first} (${String.format("%.2f", pair.second)})"
    }

    // Tensorflow Lite Example에서 기본적으로 제공하는 자세 분석 기능(코브라, 의자, 전사자세)
    private fun isPoseClassifier() {
        cameraSource?.setClassifier(if (isClassifyPose) PoseClassifier.create(requireContext()) else null)
    }

    // Initialize spinners to let user select model/accelerator/tracker.
    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tfe_pe_models_array,
            android.R.layout.simple_spinner_item,
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spnModel.adapter = adapter
            spnModel.onItemSelectedListener = changeModelListener
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tfe_pe_device_name,
            android.R.layout.simple_spinner_item,
        ).also { adaper ->
            adaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spnDevice.adapter = adaper
            spnDevice.onItemSelectedListener = changeDeviceListener
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.tfe_pe_tracker_array,
            android.R.layout.simple_spinner_item,
        ).also { adaper ->
            adaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spnTracker.adapter = adaper
            spnTracker.onItemSelectedListener = changeTrackerListener
        }
    }

    // Change model when app is running
    private fun changeModel(position: Int) {
        if (modelPos == position) return
        modelPos = position
        createPoseEstimator()
    }

    // Change device (accelerator) type when app is running
    private fun changeDevice(position: Int) {
        val targetDevice = when (position) {
            0 -> Device.CPU
            1 -> Device.GPU
            else -> Device.NNAPI
        }
        if (device == targetDevice) return
        device = targetDevice
        createPoseEstimator()
    }

    // Change tracker for Movenet MultiPose model
    private fun changeTracker(position: Int) {
        cameraSource?.setTracker(
            when (position) {
                1 -> TrackerType.BOUNDING_BOX
                2 -> TrackerType.KEYPOINTS
                else -> TrackerType.OFF
            },
        )
    }

    // 자세 추정 모델 실행 (Movenet Thunder, CPU가 적절)
    private fun createPoseEstimator() {
        // For MoveNet MultiPose, hide score and disable pose classifier as the model returns
        // multiple Person instances.
        val poseDetector = when (modelPos) {
            0 -> {
                // MoveNet Lightning (SinglePose)
                showPoseClassifier(true)
                showDetectionScore(true)
                showTracker(false)
                MoveNet.create(requireContext(), device, ModelType.Lightning)
            }
            1 -> {
                // MoveNet Thunder (SinglePose)
                showPoseClassifier(true)
                showDetectionScore(true)
                showTracker(false)
                MoveNet.create(requireContext(), device, ModelType.Thunder)
            }
            2 -> {
                // MoveNet (Lightning) MultiPose
                showPoseClassifier(false)
                showDetectionScore(false)
                // Movenet MultiPose Dynamic does not support GPUDelegate
                if (device == Device.GPU) {
                    showToast("aaaa")
                }
                showTracker(true)
                MoveNetMultiPose.create(
                    requireContext(),
                    device,
                    Type.Dynamic,
                )
            }
            3 -> {
                // PoseNet (SinglePose)
                showPoseClassifier(true)
                showDetectionScore(true)
                showTracker(false)
                PoseNet.create(requireContext(), device)
            }
            else -> {
                null
            }
        }
        poseDetector?.let { detector ->
            cameraSource?.setDetector(detector)
        }
    }

    // Show/hide the pose classification option.
    private fun showPoseClassifier(isVisible: Boolean) {
        vClassificationOption.visibility = if (isVisible) View.VISIBLE else View.GONE
        if (!isVisible) {
            swClassification.isChecked = false
        }
    }

    // Show/hide the detection score.
    private fun showDetectionScore(isVisible: Boolean) {
        tvScore.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    // Show/hide classification result.
    private fun showClassificationResult(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        tvClassificationValue1.visibility = visibility
        tvClassificationValue2.visibility = visibility
        tvClassificationValue3.visibility = visibility
    }

    // Show/hide the tracking options.
    private fun showTracker(isVisible: Boolean) {
        if (isVisible) {
            // Show tracker options and enable Bounding Box tracker.
            vTrackerOption.visibility = View.VISIBLE
            spnTracker.setSelection(1)
        } else {
            // Set tracker type to off and hide tracker option.
            vTrackerOption.visibility = View.GONE
            spnTracker.setSelection(0)
        }
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
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
