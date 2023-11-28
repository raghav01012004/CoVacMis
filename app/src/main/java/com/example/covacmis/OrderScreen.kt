package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.Request
import com.android.volley.TimeoutError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class OrderScreen : AppCompatActivity() {
    interface RecyclerViewReadyCallback {
        fun onRecyclerViewReady()
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var hospitalInfo:HospitalLogin
    private lateinit var orderList:ArrayList<Order>
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var orderOverlay : FrameLayout
    private lateinit var noOrdersTextView: TextView
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_screen)
        orderOverlay = findViewById(R.id.orderOverlay)

        recyclerView = findViewById(R.id.orderRecycler)
        noOrdersTextView = findViewById(R.id.noOrdersTextView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        hospitalInfo = intent.getSerializableExtra("hospitalDetail") as HospitalLogin
        orderList = arrayListOf()
        getOrders(hospitalInfo.hospitalName)
    }

    fun updateNoOrdersVisibility(visibility: Int) {
        noOrdersTextView.visibility = visibility
    }

    fun updateRecyclerViewVisibility(visibility: Int){
        recyclerView.visibility = visibility
    }


    private fun getOrders(hospitalName:String){
        orderOverlay.visibility = View.VISIBLE
//        orderList.clear()
        val url = "https://covacmis.onrender.com/hospital/getOrders/$hospitalName"
        val request = JsonObjectRequest(
            Request.Method.GET,url,null,
            { response->
                for (key in response.keys()){
                    val orderData = response.getJSONObject(key)
//                    println(orderData)
                    var userName:String
                    var vaccine:String
                    for(user in orderData.keys()){
                        userName = user.toString()
                        val details = orderData.getJSONObject(user)
                        vaccine = details["vaccine_name"].toString()
                        val order = Order(user,vaccine,key)
//                        println(order)
                        orderList.add(order)
                    }
                }
                val orderAdapter = OrderAdapter(orderList,hospitalInfo,recyclerView,noOrdersTextView)
                recyclerView.adapter = orderAdapter
                orderOverlay.visibility = View.GONE
                if (orderList.isEmpty()) {
                    noOrdersTextView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    noOrdersTextView.visibility = View.GONE
                }
                (applicationContext as? RecyclerViewReadyCallback)?.onRecyclerViewReady()
            },
            {
               error->
                if(error is TimeoutError){
                    orderOverlay.visibility = View.GONE
                    Toast.makeText(this, "Login timed out. Please try again!", Toast.LENGTH_SHORT).show()
                }
                else if(error is NoConnectionError){
                    orderOverlay.visibility = View.GONE
                    Toast.makeText(this, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                }
                else if(error is NetworkError){
                    orderOverlay.visibility = View.GONE
                    Toast.makeText(this, "No internet. Please check your internet connection", Toast.LENGTH_SHORT).show()
                }
                startActivity(Intent(this,MainActivity::class.java))
            }
        )
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finishAffinity()
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press again to exit", Toast.  LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

}