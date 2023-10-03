package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val textView = findViewById<TextView>(R.id.textView4)
        val username = findViewById<EditText>(R.id.editText)
        val password = findViewById<EditText>(R.id.editText2)
        val button = findViewById<Button>(R.id.button)



        button.setOnClickListener {
            val uname = username.getText()
            val pass = password.getText()

            if(uname.toString() == "" || pass.toString() == "")
            {
                Toast.makeText(applicationContext, "Please fill the above fields", Toast.LENGTH_SHORT).show()
            }

        }
        textView.setOnClickListener {
            Toast.makeText(applicationContext, "Wait a sec...", Toast.LENGTH_SHORT).show()

            intent = Intent(applicationContext,MainActivity2::class.java)
            startActivity(intent)
        }

    }
}