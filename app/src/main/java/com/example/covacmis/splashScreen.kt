package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class splashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val logo = findViewById<ImageView>(R.id.splashLogo)
        val design = findViewById<ImageView>(R.id.splashDesign)

        logo.alpha = 0f
        logo.animate().setDuration(1500).alpha(1f).withEndAction{
            val showLogo = Intent(this,MainActivity::class.java)
            startActivity(showLogo)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
    }
}