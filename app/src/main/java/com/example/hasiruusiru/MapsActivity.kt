package com.example.hasiruusiru

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_maps)

        val mapFragment =
            supportFragmentManager
                .findFragmentById(R.id.map)
                    as SupportMapFragment

        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        val bangalore =
            LatLng(12.9716, 77.5946)

        // Neem Tree

        mMap.addMarker(
            MarkerOptions()
                .position(
                    LatLng(12.9716, 77.5946)
                )
                .title("🌳 Neem Tree")
                .snippet("High Oxygen Tree")
                .icon(
                    BitmapDescriptorFactory
                        .defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN
                        )
                )
        )

        // Peepal Tree

        mMap.addMarker(
            MarkerOptions()
                .position(
                    LatLng(12.9750, 77.5990)
                )
                .title("🌳 Peepal Tree")
                .snippet("Very High Oxygen")
                .icon(
                    BitmapDescriptorFactory
                        .defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN
                        )
                )
        )

        // Honge Tree

        mMap.addMarker(
            MarkerOptions()
                .position(
                    LatLng(12.9680, 77.5900)
                )
                .title("🌳 Honge Tree")
                .snippet("Medium Oxygen Tree")
                .icon(
                    BitmapDescriptorFactory
                        .defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN
                        )
                )
        )

        // Empty Pit

        mMap.addMarker(
            MarkerOptions()
                .position(
                    LatLng(12.9735, 77.5925)
                )
                .title("❌ Empty Pit")
                .snippet("Needs Plantation")
                .icon(
                    BitmapDescriptorFactory
                        .defaultMarker(
                            BitmapDescriptorFactory.HUE_RED
                        )
                )
        )

        // CAMERA

        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                bangalore,
                13f
            )
        )

        // MAP SETTINGS

        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.mapType =
            GoogleMap.MAP_TYPE_HYBRID
    }
}