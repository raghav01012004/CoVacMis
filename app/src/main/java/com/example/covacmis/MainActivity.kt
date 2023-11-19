package com.example.covacmis

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.TimeoutError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class MainActivity : AppCompatActivity(),OrderScreen.RecyclerViewReadyCallback {

    private lateinit var mainOverlay:FrameLayout
    private lateinit var button: Button
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var sw1: Switch
    private lateinit var message:String
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainOverlay = findViewById(R.id.loginOverlay)
        mainOverlay.visibility = View.GONE

        val textView = findViewById<TextView>(R.id.textView4)
        val username = findViewById<EditText>(R.id.editText)
        val password = findViewById<EditText>(R.id.editText2)
        button = findViewById(R.id.button)

        message = "Individual"

        button.setOnClickListener {
            val uname = username.text?.toString()
            val pass = password.text?.toString()

            if (uname.isNullOrEmpty() || pass.isNullOrEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please fill the above fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if(message=="Individual"){
                    getData(uname,pass)
                }
                else{
                    getHospitalData(uname,pass)
                }
            }

        }
        textView.setOnClickListener {
            Toast.makeText(applicationContext, "Wait a sec...", Toast.LENGTH_SHORT).show()

            intent = Intent(applicationContext, MainActivity2::class.java)
            startActivity(intent)
        }

        sw1 = findViewById(R.id.hospitalSwitch)
        sw1.setOnCheckedChangeListener { _, isChecked ->
            message = if (isChecked) "Hospital" else "Individual"
            Toast.makeText(this@MainActivity, "Identifying as : " + message, Toast.LENGTH_SHORT)
                .show()
        }

    }

    private fun getData(username: String, password: String) {

        mainOverlay.visibility = View.VISIBLE
        button.isEnabled = false

        // Assuming you have `username` and `password` as Strings
        val url = "https://covacmis.onrender.com/login/$username/$password"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val user = Gson().fromJson(response.toString(), User::class.java)
//                for(i in response.keys()){
//                    println(response[i])
//                }
                if (user?.fullname != null && user.fullname.isNotEmpty()) {
                    val intent = Intent(applicationContext, VaccinationChart::class.java).putExtra("user",user)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT)
                        .show()
                }
                button.isEnabled = true
            },
            { error ->
                if(error is TimeoutError){
                    mainOverlay.visibility = View.GONE
                    button.isEnabled = true
                    Toast.makeText(this, "Login timed out. Please try again!", Toast.LENGTH_SHORT).show()
                }
                else if(error is NoConnectionError){
                    mainOverlay.visibility = View.GONE
                    button.isEnabled = true
                    Toast.makeText(this, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                }
                else if(error is NetworkError){
                    mainOverlay.visibility = View.GONE
                    button.isEnabled = true
                    Toast.makeText(this, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                }
            })
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    private fun getHospitalData(hospitalName: String, password: String) {

        mainOverlay.visibility = View.VISIBLE
        button.isEnabled = false

        // Assuming you have `username` and `password` as Strings
        val url = "https://covacmis.onrender.com/hospital/login/$hospitalName/$password"
        println(url)
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val hospital = Gson().fromJson(response.toString(), HospitalLogin::class.java)
                println(hospital)
//                for(i in response.keys()){
//                    println(response[i])
//                }
                if (hospital?.hospitalId != null && hospital.hospitalId.isNotEmpty()) {
                    val intent = Intent(applicationContext, OrderScreen::class.java).putExtra("hospitalDetail",hospital)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT)
                        .show()
                }
                mainOverlay.visibility = View.GONE
                button.isEnabled = true
            },
            { error ->
                if(error is TimeoutError){
                    mainOverlay.visibility = View.GONE
                    button.isEnabled = true
                    Toast.makeText(this, "Login timed out. Please try again!", Toast.LENGTH_SHORT).show()
                }
                else if(error is NoConnectionError){
                    mainOverlay.visibility = View.GONE
                    button.isEnabled = true
                    Toast.makeText(this, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                }
                else if(error is NetworkError){
                    mainOverlay.visibility = View.GONE
                    button.isEnabled = true
                    Toast.makeText(this, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                }
            })
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    override fun onRecyclerViewReady() {
        runOnUiThread {
            // Add any specific actions you want to perform here
            Toast.makeText(this, "RecyclerView in OrderScreen is fully populated!", Toast.LENGTH_SHORT).show()
        }
    }
}
