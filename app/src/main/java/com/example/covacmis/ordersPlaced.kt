package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class ordersPlaced : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_placed)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.bottom_Placed

        bottomNavigationView.setOnNavigationItemSelectedListener {
                menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    val intent = Intent(this, VaccinationChart::class.java)
                    startActivity(intent)
                    true
                }
                R.id.bottom_completed -> {
                    val intent = Intent(this, completed::class.java)
                    startActivity(intent)
                    true
                }
                R.id.bottom_Placed -> {
                    true
                }
                else -> false
            }
        }

    }
}