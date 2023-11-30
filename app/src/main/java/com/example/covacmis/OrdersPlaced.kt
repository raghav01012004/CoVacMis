package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.TimeoutError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView

class OrdersPlaced : AppCompatActivity() {

    private lateinit var userInfo:User
    private lateinit var recyclerView:RecyclerView
    private lateinit var placedOverlay:FrameLayout
    private lateinit var placedOrderList:ArrayList<PlacedOrder>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_placed)

        userInfo = intent.getSerializableExtra("user") as User
        placedOverlay = findViewById(R.id.placedOverlay)
        placedOverlay.visibility = View.VISIBLE
        recyclerView = findViewById(R.id.recyclerView3)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutPlaced)
        swipeRefreshLayout.isRefreshing = false
        swipeRefreshLayout.setOnRefreshListener {
            val intent = Intent(this,OrdersPlaced::class.java)
            intent.putExtra("user",userInfo)
            startActivity(intent)
            swipeRefreshLayout.isRefreshing=false
        }

        placedOrderList= arrayListOf()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.bottom_Placed

        bottomNavigationView.setOnNavigationItemSelectedListener {
                menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    val intent = Intent(this, VaccinationChart::class.java).putExtra("user",userInfo)
                    startActivity(intent)
                    true
                }
                R.id.bottom_completed -> {
                    val intent = Intent(this, Completed::class.java).putExtra("user",userInfo)
                    startActivity(intent)
                    true
                }
                R.id.bottom_Placed -> {
                    true
                }
                else -> false
            }
        }
        getPlacedOrderList(userInfo.username)

    }

    private fun getPlacedOrderList(username:String){
        placedOverlay.visibility - View.VISIBLE
        placedOrderList.clear()
        val url = "https://covacmis.onrender.com/user/orders/$username"

        val jsonRequest = JsonObjectRequest(
            Request.Method.GET,url,null,
            {
                    response->
                val resObj = response.getJSONObject("orders")
                for(key in resObj.keys()){
                    val getOrderObj = resObj.getJSONObject(key)
                    val hosName = getOrderObj["hospital_name"].toString()
                    val ordDate = getOrderObj["date"].toString()
                    val ordBrand = getOrderObj["brand_name"].toString()
                    val orderObj = PlacedOrder(key,hosName,ordDate,ordBrand)
                    placedOrderList.add(orderObj)
                }
                if(placedOrderList.isEmpty()){

                }
                else{
                    val myAdapter = PlacedOrderAdapter(placedOrderList)
                    recyclerView.adapter = myAdapter
                }
                placedOverlay.visibility = View.GONE
            },
            {
                    error->
                if(error is TimeoutError){
                    placedOverlay.visibility = View.GONE
                    Toast.makeText(this, "Login timed out. Please try again!", Toast.LENGTH_SHORT).show()
                }
                else if(error is NoConnectionError){
                    placedOverlay.visibility = View.GONE
                    Toast.makeText(this, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                }
                else if(error is NetworkError){
                    placedOverlay.visibility = View.GONE
                    Toast.makeText(this, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                }
            }
        )
        val queue = Volley.newRequestQueue(this)
        queue.add(jsonRequest)
    }
}