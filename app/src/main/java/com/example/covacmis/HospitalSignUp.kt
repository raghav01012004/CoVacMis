package com.example.covacmis

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.TimeoutError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

class HospitalSignUp : AppCompatActivity() {

    interface LocationCallback {
        fun onLocationFetched(latitude: String, longitude: String, address: String)
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var address: String
    private lateinit var signUpOverlay:FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_sign_up)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val button = findViewById<Button>(R.id.button5)

        val name = findViewById<EditText>(R.id.editTextText)
        val inputAddress= findViewById<EditText>(R.id.editTextText2)
        val pass = findViewById<EditText>(R.id.editTextText4)
        val mobile = findViewById<EditText>(R.id.editTextText3)
        signUpOverlay = findViewById(R.id.hosSignUpOverlay)
        latitude="0.0"
        longitude="0.0"
        address="Surat"


        button.setOnClickListener {
            signUpOverlay.visibility = View.VISIBLE
            button.isEnabled = false
            val fullname = name.text?.toString()
            val addr = inputAddress.text?.toString()
            val passw = pass.text?.toString()
            val mob = mobile.text?.toString()

            if (fullname.isNullOrEmpty() || addr.isNullOrEmpty() || passw.isNullOrEmpty() || mob.isNullOrEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please fill the above fields",
                    Toast.LENGTH_SHORT
                ).show()
                signUpOverlay.visibility = View.GONE
                button.isEnabled = true
            }
            //check the length of mobile no. -->
            else if(mob.length != 10)
            {
                Toast.makeText(applicationContext, "Invalid Mobile no. !", Toast.LENGTH_SHORT).show()
                signUpOverlay.visibility = View.GONE
                button.isEnabled = true
            }
            else if(passw.length < 6)
            {
                Toast.makeText(applicationContext, "Password should be at-least 6 characters", Toast.LENGTH_SHORT).show()
                signUpOverlay.visibility = View.GONE
                button.isEnabled = true
            }
            else {
                fetchLocation(object : LocationCallback {
                    override fun onLocationFetched(latitude: String, longitude: String, address: String) {
                        println("$longitude $latitude")
                        val locationArray = arrayListOf(latitude,longitude)
                        println(locationArray)
                        val requestBody = JSONObject().apply {
                            put("hospitalName", fullname)
                            put("location", JSONArray(locationArray))
                            put("password", passw)
                            put("mob", mob)
                            put("address", address)
                            put("fullAddr", addr)
                        }
                        println(requestBody)
                        val url = "https://covacmis.onrender.com/hospital/create"
                        val request = JsonObjectRequest(
                            Request.Method.POST, url, requestBody,
                            { response ->
                                if(response["success"]==1){
                                    val hospitalId = response["hospitalId"].toString()
                                    val hospitalName = response["hospitalName"].toString()
                                    val hospitalData = HospitalLogin(hospitalId,hospitalName)
                                    val intent = Intent(applicationContext,OrderScreen::class.java).putExtra("hospitalDetail",hospitalData)
                                    startActivity(intent)
                                    signUpOverlay.visibility = View.GONE
                                }

                                else{
                                    Toast.makeText(applicationContext,"Hospital already exists. Moving to Login",Toast.LENGTH_SHORT).show()
                                    signUpOverlay.visibility = View.GONE
                                    startActivity(Intent(applicationContext,MainActivity::class.java))
                                }
                            },
                            { error ->
                                if(error is TimeoutError){
                                    signUpOverlay.visibility = View.GONE
                                    button.isEnabled = true
                                    Toast.makeText(applicationContext, "Login timed out. Please try again!", Toast.LENGTH_SHORT).show()
                                }
                                else if(error is NoConnectionError){
                                    signUpOverlay.visibility = View.GONE
                                    button.isEnabled = true
                                    Toast.makeText(applicationContext, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                                }
                                else if(error is NetworkError){
                                    signUpOverlay.visibility = View.GONE
                                    button.isEnabled = true
                                    Toast.makeText(applicationContext, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                        val queue = Volley.newRequestQueue(this@HospitalSignUp)
                        queue.add(request)
                    }
                })
            }

        }

    }

    private fun fetchLocation(callback: LocationCallback) {
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                latitude = it.latitude.toString()
                longitude = it.longitude.toString()
                address = getAddressName(it.latitude, it.longitude)
                callback.onLocationFetched(latitude, longitude, address)
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