package com.example.covacmis


import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlin.properties.Delegates

internal class gMap : AppCompatActivity(), OnMapReadyCallback {

    private final var FINE_PERMISSION_CODE = 1
    private lateinit var myMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitude:Double = 0.0
    private var longitude :Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_gmap)

        latitude = intent.getDoubleExtra("latitude",0.0)
        longitude = intent.getDoubleExtra("longitude",0.0)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

    }

    private fun fetchLocation()
    {
        val task = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),FINE_PERMISSION_CODE)
            return
        }
        task.addOnSuccessListener() {
            if(it != null){
                val textLatitude = "Latitude : " + it.latitude.toString()
                val textLongitude = "Longitude : " + it.longitude.toString()
                Toast.makeText(applicationContext, "Latitude is : ${it.latitude} \n AND \n Longitude is : ${it.longitude}", Toast.LENGTH_SHORT).show()
                currentLocation = it

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.


                val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(latitude,longitude)
        myMap.addMarker(MarkerOptions().position(sydney).title("Your Location"))
        myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == FINE_PERMISSION_CODE)
        {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                fetchLocation()
            }
            else
            {
                Toast.makeText(applicationContext, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

