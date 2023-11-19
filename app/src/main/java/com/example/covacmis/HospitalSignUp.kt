package com.example.covacmis

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class HospitalSignUp : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var address: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_sign_up)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val button = findViewById<Button>(R.id.button5)

        val name = findViewById<EditText>(R.id.editTextText)
        val uname = findViewById<EditText>(R.id.editTextText2)
        val pass = findViewById<EditText>(R.id.editTextText4)
        val mobile = findViewById<EditText>(R.id.editTextText3)


        button.setOnClickListener {

            val fullname = name.text?.toString()
            val username = uname.text?.toString()
            val passw = pass.text?.toString()
            val mob = mobile.text?.toString()

            if (fullname.isNullOrEmpty() || username.isNullOrEmpty() || passw.isNullOrEmpty() || mob.isNullOrEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please fill the above fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
            //check the length of mobile no. -->
            else if(mob.length != 10)
            {
                Toast.makeText(applicationContext, "Invalid Mobile no. !", Toast.LENGTH_SHORT).show()
            }
            else if(passw.length < 6)
            {
                Toast.makeText(applicationContext, "Password should be at-least 6 characters", Toast.LENGTH_SHORT).show()
            }
            else{

            fetchLocation()
            intent = Intent(applicationContext,hospitalList::class.java)
            startActivity(intent)
            }
        }

    }

    private fun fetchLocation()
    {
        val task = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
            return
        }
        task.addOnSuccessListener {
            if(it != null){
                val textLatitude = "Latitude : " + it.latitude.toString()
                val textLongitude = "Longitude : " + it.longitude.toString()
                val textaddress = "Address : " + getAddressName(it.latitude, it.longitude)
                Toast.makeText(applicationContext, "Latitude is : ${it.latitude} \n AND \n Longitude is : ${it.longitude}", Toast.LENGTH_SHORT).show()
//                Toast.makeText(applicationContext, "City is : $textaddress", Toast.LENGTH_SHORT).show()
                latitude = textLatitude
                longitude = textLongitude
                address = textaddress
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getAddressName(lat: Double, lon:Double): String{

        var addressName = ""
        val geoCoder = Geocoder(this, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat,lon,1)

        if (address != null) {
            addressName = address[0].locality
        }
        return addressName
    }

}