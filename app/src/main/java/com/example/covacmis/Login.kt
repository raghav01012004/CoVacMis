package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.editTextText3)
        val password = findViewById<EditText>(R.id.editTextTextPassword)
        val button = findViewById<Button>(R.id.button6)
        val signup = findViewById<TextView>(R.id.textView9)

        button.setOnClickListener(){
            val uname = username.text
            val pass = password.text

            if(uname.toString() == "" || pass.toString() == "" ){
                Toast.makeText(applicationContext,"Please fill the above fields",Toast.LENGTH_SHORT).show()
            }

        }

        signup.setOnClickListener(){
            Toast.makeText(applicationContext, "Wait a sec...", Toast.LENGTH_SHORT).show()

            intent = Intent(applicationContext,MainActivity2::class.java)
            startActivity(intent)
        }

    }
}