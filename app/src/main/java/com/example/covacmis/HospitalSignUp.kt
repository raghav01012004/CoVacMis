package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class HospitalSignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_sign_up)

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


            Toast.makeText(applicationContext,"Suiiii",
                Toast.LENGTH_SHORT).show()
            intent = Intent(applicationContext,hospitalList::class.java)
            startActivity(intent)
            }
        }

    }
}