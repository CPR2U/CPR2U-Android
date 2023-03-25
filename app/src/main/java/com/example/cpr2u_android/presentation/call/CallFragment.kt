package com.example.cpr2u_android.presentation.call

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.FragmentCallBinding
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class CallFragment : Fragment(), OnMapReadyCallback, LocationListener {
    private lateinit var binding: FragmentCallBinding
    private val locationPermissionCode = 100
    lateinit var mapFragment: MapView

    private lateinit var mMap: GoogleMap
    private lateinit var mLocationManager: LocationManager
    private lateinit var mMarker: Marker
    private lateinit var progressBell: ProgressBar
    private lateinit var fadeIn: View
    private lateinit var fadeInAnim: Animation
    private lateinit var fadeInText: TextView
    private var timerStarted = false
    private var timeLeftInMillis = 0L
    private lateinit var countDownTimer: CountDownTimer

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentCallBinding.inflate(layoutInflater, container, false)
        val view: View = inflater.inflate(R.layout.fragment_call, container, false)

        MapsInitializer.initialize(requireContext(), MapsInitializer.Renderer.LATEST) {
            // println(it.name)
        }
        mapFragment = view.findViewById<MapView>(R.id.mapFragment)
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync(this)

        val bell = view.findViewById<ImageView>(R.id.iv_bell)
        progressBell = view.findViewById<ProgressBar>(R.id.progress_bar_bell)
        progressBell.visibility = View.GONE

        fadeIn = view.findViewById<View>(R.id.fade_in)
        fadeInAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeInText = view.findViewById<TextView>(R.id.tv_fade_in)
        fadeIn.visibility = View.INVISIBLE
        progressBell.visibility = View.INVISIBLE
        fadeInText.visibility = View.INVISIBLE

        bell.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    fadeIn.startAnimation(fadeInAnim)
                    fadeIn.setBackgroundColor(Color.parseColor("#42FF2F2F"))
                    startTimer()
                }

                MotionEvent.ACTION_UP -> {
                    fadeIn.visibility = View.INVISIBLE
                    progressBell.visibility = View.INVISIBLE
                    fadeInText.visibility = View.INVISIBLE
                    fadeIn.clearAnimation()
                    resetTimer()
                }
            }
            true
        }
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode,
            )
            return
        }
        mMap = googleMap ?: return
        mMap.isMyLocationEnabled = true

        // 처음 화면 서울 -> 현재 위치 버튼 눌러야 내위치 이동
        val posGps: LatLng = LatLng(37.5642135, 127.0016985)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posGps, 15f))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)

        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        mLocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
//        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)
    }

    private fun startTimer() {
        if (!timerStarted) {
            countDownTimer = object : CountDownTimer(4000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    fadeIn.visibility = View.VISIBLE
                    progressBell.visibility = View.VISIBLE
                    fadeInText.visibility = View.VISIBLE
                    timeLeftInMillis = millisUntilFinished
                    val secondsLeft = timeLeftInMillis / 1000
                    fadeInText.text = secondsLeft.toString()
                }

                override fun onFinish() {
                    fadeIn.visibility = View.INVISIBLE
                    progressBell.visibility = View.INVISIBLE
                    fadeInText.visibility = View.INVISIBLE
                    startActivity(Intent(requireContext(), CallingActivity::class.java))
                }
            }.start()

            timerStarted = true
        }
    }

    private fun resetTimer() {
        if (timerStarted) {
            countDownTimer.cancel()
            timerStarted = false
            timeLeftInMillis = 0L
            fadeInText.text = "0"
        }
    }

    override fun onStart() {
        super.onStart()
        mapFragment.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapFragment.onStop()
        resetTimer()
    }

    override fun onResume() {
        super.onResume()
        mapFragment.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapFragment
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onLocationChanged(location: Location) {
        location ?: return

        // Add a marker for the user's current location
        if (::mMarker.isInitialized) {
            mMarker.remove()
        }
        mMarker =
            mMap.addMarker(
                MarkerOptions().position(LatLng(location.latitude, location.longitude))
                    .title("Current Location"),
            )!!
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    location.latitude,
                    location.longitude,
                ),
                15f,
            ),
        )

        // Stop updating the user's location to save battery
//        mLocationManager.removeUpdates(this)
    }

    companion object {
        /** Long Press 판단 기준 시간 */
        private const val LONG_PRESSED_TIME = 2L
    }
}
