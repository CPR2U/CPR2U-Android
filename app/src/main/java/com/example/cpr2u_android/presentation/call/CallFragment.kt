package com.example.cpr2u_android.presentation.call

import android.Manifest
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.cpr2u_android.R
import com.example.cpr2u_android.databinding.FragmentCallBinding
import com.example.cpr2u_android.presentation.auth.LoginActivity
import com.example.cpr2u_android.presentation.education.LectureActivity
import com.example.cpr2u_android.presentation.education.QuizActivity
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class CallFragment : Fragment(), OnMapReadyCallback, LocationListener {
    private lateinit var binding: FragmentCallBinding
    private lateinit var map: GoogleMap
    private lateinit var handler: Handler
    private lateinit var objectAnimator: ObjectAnimator
    private val locationPermissionCode = 100
    lateinit var mapFragment: MapView

    private lateinit var mMap: GoogleMap
    private lateinit var mLocationManager: LocationManager
    private lateinit var mMarker: Marker
    private lateinit var progressBell: ProgressBar

    private var longClickHandler = Handler()
    private var longClickRunnable = Runnable {
        progressBell.visibility = View.GONE
        startActivity(Intent(requireContext(), CallingActivity::class.java))
    }

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

        bell.setOnTouchListener { _, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    progressBell.visibility = View.VISIBLE
                    longClickHandler.postDelayed(longClickRunnable, 3000)
                }
                MotionEvent.ACTION_UP -> {
                    progressBell.visibility = View.GONE
                    longClickHandler.removeCallbacks(longClickRunnable)
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

    override fun onStart() {
        super.onStart()
        mapFragment.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapFragment.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapFragment.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapFragment
        longClickHandler.removeCallbacks(longClickRunnable)
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
