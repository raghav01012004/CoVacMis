package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val button = findViewById<Button>(R.id.button2)
        val button2 = findViewById<Button>(R.id.button3)
        val imageview1 = findViewById<ImageView>(R.id.imageView2)
        val imageview2 = findViewById<ImageView>(R.id.imageView3)

        button.setOnClickListener {
            imageview1.animate().apply {
                duration = 1000
                rotationYBy(360f)
            }.start()
            Handler(Looper.getMainLooper()).postDelayed({
                intent = Intent(applicationContext,ParentSignUp::class.java)
                startActivity(intent)
            }, 1000)
        }
        button2.setOnClickListener {
            imageview2.animate().apply {
                duration = 1000
                rotationYBy(360f)
            }.start()
            Handler(Looper.getMainLooper()).postDelayed({
                intent = Intent(applicationContext,HospitalSignUp::class.java)
                startActivity(intent)
            }, 1000)

        }

    }
}