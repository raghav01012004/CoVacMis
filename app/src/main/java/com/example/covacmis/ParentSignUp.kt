package com.example.covacmis

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.TimeoutError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class ParentSignUp : AppCompatActivity() {

    private lateinit var signUpFrameLayout: FrameLayout
    @SuppressLint("MissingInflatedId", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_sign_up)

        signUpFrameLayout = findViewById(R.id.signUpOverlay)
        signUpFrameLayout.visibility = View.GONE

        val button = findViewById<Button>(R.id.button4)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val radioGroup2 = findViewById<RadioGroup>(R.id.radioGroup2)
        val name = findViewById<EditText>(R.id.editText3)
        val uname = findViewById<EditText>(R.id.editText4)
        val pass = findViewById<EditText>(R.id.editText5)
        val mobile = findViewById<EditText>(R.id.editText6)
        val dob = findViewById<EditText>(R.id.editText7)
        val vaccines : Map<String,Any> = mapOf()

        // Here are present day, month & year
        val calender = Calendar.getInstance()
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH) + 1
        val day = calender.get(Calendar.DAY_OF_MONTH)

        //Below Code is for Prompt to collect D.O.B
        val btnDatePicker = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        var date: String
        var sd: String
        var sm: String
        var sy: String
        // when floating action button is clicked
        btnDatePicker.setOnClickListener {
            // Initiation date picker with
            // MaterialDatePicker.Builder.datePicker()
            // and building it using build()
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")

            // Setting up the event for when ok is clicked
            datePicker.addOnPositiveButtonClickListener {
                // formatting date in dd-mm-yyyy format.
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val yearFormatter = SimpleDateFormat("yyyy")
                val monthFormatter = SimpleDateFormat("MM")
                val dayFormatter = SimpleDateFormat("dd")

                //Collecting Date in all formats
                date = dateFormatter.format(Date(it))
                sy = yearFormatter.format(Date(it))
                sm = monthFormatter.format(Date(it))
                sd = dayFormatter.format(Date(it))
                val age = ageCalc(sy.toInt(),sm.toInt(),year,month)
                Toast.makeText(this, "$date is selected. Age is $age", Toast.LENGTH_LONG).show()
                dob.setText(date)
            }

            // Setting up the event for when cancelled is clicked
            datePicker.addOnNegativeButtonClickListener {
                Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG).show()
            }

            // Setting up the event for when back button is pressed
            datePicker.addOnCancelListener {
                Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
            }
        }


//        val gender = findViewById<EditText>(R.id.editText8)
        button.setOnClickListener {

            signUpFrameLayout.visibility = View.VISIBLE
            button.isEnabled = false
            val radioUser = findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
            val radioGender = findViewById<RadioButton>(radioGroup2.checkedRadioButtonId)
//            Toast.makeText(applicationContext,"Registering As : ${radioUser.text}",
//                Toast.LENGTH_SHORT).show()
//            Toast.makeText(this, "${radioGender.text}", Toast.LENGTH_SHORT).show()


            val user = User(
                fullname = name.text.toString(),
                username = uname.text.toString(),
                dob = dob.text.toString(),
                password = pass.text.toString(),
                gender = radioGender.text.toString(),
                mobile_no = mobile.text.toString(),
                vaccines = vaccines
            )

            val requestBody = JSONObject().apply {
                put("fullname", user.fullname)
                put("username", user.username)
                put("dob", user.dob)
                put("password", user.password)
                put("gender", user.gender)
                put("mobile_no", user.mobile_no)
                put("vaccines",JSONObject(user.vaccines))
            }

            val url = "https://covacmis.onrender.com/create" // Replace with your endpoint URL

            val request = JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                { response ->
                    if(response!=null){
                        Toast.makeText(applicationContext,"Welcome ${user.fullname} to CoVacMis...",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext,VaccinationChart::class.java).putExtra("user",user))
                    }

                    signUpFrameLayout.visibility = View.GONE
                    button.isEnabled = true

                },
                { error ->
                        if(error is TimeoutError){
                        signUpFrameLayout.visibility = View.GONE
                        button.isEnabled = true
                        Toast.makeText(this, "Server timed out. Please try again later!", Toast.LENGTH_SHORT).show()
                    }
                    else if(error is NoConnectionError){
                        signUpFrameLayout.visibility = View.GONE
                        button.isEnabled = true
                        Toast.makeText(this, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                    }
                    else if(error is NetworkError){
                        signUpFrameLayout.visibility = View.GONE
                        button.isEnabled = true
                        Toast.makeText(this, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                    }
                })

// Add the request to the RequestQueue
            val queue = Volley.newRequestQueue(this)
            queue.add(request)

        }
    }
    private fun ageCalc(sy: Int, sm: Int, cy: Int, cm: Int): Int {
        return if (sm > cm)
            cy - sy - 1
        else
            cy - sy
    }
}