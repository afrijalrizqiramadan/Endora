package com.asyabab.endora.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.asyabab.endora.R
import com.asyabab.endora.utils.onClick
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.lang.Double.parseDouble


class MapsActivity : FragmentActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var marker: Marker? = null
    private var lat: String? = null
    private var long: String? = null
    lateinit var latlng:LatLng

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if (intent.getStringExtra("latitude") != null) {
            lat = intent.getStringExtra("latitude")
            long=intent.getStringExtra("longitude")


        }

        btBack.onClick {
            finish()
        }

        btPilihLokasi.onClick {
            if (lat==null){
                Toast.makeText(
                    this@MapsActivity,
                    "Pilih Lokasi Terlebih Dahulu",
                    Toast.LENGTH_LONG
                ).show()
            }else{
                val intent = Intent()
                intent.putExtra("lat",lat)
                intent.putExtra("long",long)
                setResult(RESULT_OK, intent);
                finish()
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap!!.uiSettings.isZoomControlsEnabled = true
        mMap!!.setOnMarkerClickListener { marker ->
            val position = marker.position
            Toast.makeText(
                this@MapsActivity,
                "Lat " + position.latitude + " "
                        + "Long " + position.longitude,
                Toast.LENGTH_LONG
            ).show()
            true
        }
        setUpMap()


        if (lat!=null) run {
            val latitude = lat?.let { parseDouble(it) };
            val longitude = long?.let { parseDouble(it) };

            val marker =
                MarkerOptions().position(LatLng(latitude!!, longitude!!)).title("Lokasi")
            googleMap.addMarker(marker)
        }
        googleMap.setOnMapClickListener { point ->
            Log.d("DEBUG", "Map clicked [" + point.latitude + " / " + point.longitude + "]")
            lat= point.latitude.toString()
            long= point.longitude.toString()
            placeMarkerOnMap(point)

        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        mMap?.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }else{
                val currentLatLng = LatLng( -6.200000, 106.816666)
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        if (marker != null) {
            marker!!.remove();
        }
        marker=mMap?.addMarker(markerOptions)
    }


}
