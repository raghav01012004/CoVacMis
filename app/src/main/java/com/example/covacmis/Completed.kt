package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.TimeoutError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.JsonObject

class Completed : AppCompatActivity() {

    private lateinit var userInfo:User
    private lateinit var completedList:ArrayList<CompletedVac>
    private lateinit var recyclerView: RecyclerView
    private lateinit var completedOverlay:FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed)

        userInfo = intent.getSerializableExtra("user") as User
        completedList= arrayListOf()
        completedOverlay = findViewById(R.id.completedOverlay)
        recyclerView = findViewById(R.id.comrecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        completedOverlay.visibility = View.VISIBLE

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.bottom_completed

        bottomNavigationView.setOnNavigationItemSelectedListener {
                menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    val intent = Intent(this, VaccinationChart::class.java).putExtra("user",userInfo)
                    startActivity(intent)
                    true
                }
                R.id.bottom_completed -> {
                    true
                }
                R.id.bottom_Placed -> {
                    val intent = Intent(this, ordersPlaced::class.java).putExtra("user",userInfo)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        getCompletedList(userInfo.username)

    }

    private fun getCompletedList(username:String){
        completedOverlay.visibility - View.VISIBLE
        val url = "https://covacmis.onrender.com/user/comVaccines/$username"

        val jsonRequest = JsonObjectRequest(
            Request.Method.GET,url,null,
            {
                response->
                    for(key in response.keys()){
                        val keyObj = response.getJSONObject(key)
                        val comDose = keyObj["dose_count"].toString()
                        val comDate = keyObj["date_of_vaccination"].toString()
                        val completedObj = CompletedVac(key,comDate,comDose)
                        completedList.add(completedObj)
                    }
                if(completedList.isEmpty()){

                }
                else{
                    val myAdapter = CompleteAdapter(completedList)
                    recyclerView.adapter = myAdapter
                }
                completedOverlay.visibility = View.GONE
            },
            {
                error->
                if(error is TimeoutError){
                    completedOverlay.visibility = View.GONE
                    Toast.makeText(this, "Login timed out. Please try again!", Toast.LENGTH_SHORT).show()
                }
                else if(error is NoConnectionError){
                    completedOverlay.visibility = View.GONE
                    Toast.makeText(this, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                }
                else if(error is NetworkError){
                    completedOverlay.visibility = View.GONE
                    Toast.makeText(this, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                }
            }
        )
        val queue = Volley.newRequestQueue(this)
        queue.add(jsonRequest)
    }
}