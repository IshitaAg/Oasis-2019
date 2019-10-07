package com.dvm.appd.oasis.dbg.more.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dvm.appd.oasis.dbg.MainActivity
import com.dvm.appd.oasis.dbg.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootPOV = inflater.inflate(R.layout.fragment_map, container, false)

        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
        return rootPOV
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity!! as MainActivity).hideCustomToolbarForLevel2Fragments()
        activity!!.search.isVisible = false
        activity!!.textView7.isVisible = false

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backBtn.setOnClickListener {
            view.findNavController().popBackStack()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        if(googleMap != null) {
            val locations = mapOf(
                "Rotunda" to LatLng(28.3633546, 75.5871163),
                "FD2 QT" to LatLng(28.364265, 75.588081),
                "FD1" to LatLng(28.364255, 75.589361),
                "FD3 QT" to LatLng(28.363734, 75.586000),
                "NAB Audi" to LatLng(28.362171, 75.587562),
                "Library" to LatLng(28.363734, 75.586000),
                "SR Grounds" to LatLng(28.365908, 75.587813),
                "LTC" to LatLng(28.365136, 75.590246)
            )

            locations.forEach {
                googleMap.addMarker(MarkerOptions().position(it.value).title(it.key)).showInfoWindow()
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations["Rotunda"], 17.0f))
        }
    }
}
