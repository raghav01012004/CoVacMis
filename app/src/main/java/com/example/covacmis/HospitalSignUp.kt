package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class HospitalSignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_sign_up)

        val button = findViewById<Button>(R.id.button5)

        button.setOnClickListener {
            Toast.makeText(applicationContext,"Suiiii",
                Toast.LENGTH_SHORT).show()
            intent = Intent(applicationContext,OtpVerification::class.java)
            startActivity(intent)
        }

    }
}