package com.example.covacmis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_screen)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)

        recyclerView = findViewById(R.id.orderRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        hospitalInfo = intent.getSerializableExtra("hospitalDetail") as HospitalLogin
        orderList = arrayListOf()
        getOrders(hospitalInfo.hospitalName)
    }


    private fun getOrders(hospitalName:String){
        loadingProgressBar.visibility = View.VISIBLE
        orderList.clear()
        val url = "http://10.0.2.2:8000/hospital/getOrders/$hospitalName"
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
                        vaccine = orderData[user].toString()
                        val order = Order(user,vaccine,key)
//                        println(order)
                        orderList.add(order)
                    }
                }
                println(orderList)
                val orderAdapter = OrderAdapter(orderList)
                recyclerView.adapter = orderAdapter
                loadingProgressBar.visibility = View.GONE
                (applicationContext as? RecyclerViewReadyCallback)?.onRecyclerViewReady()
            },
            {
               error->
                Log.d("OrderScreen",error.toString())
                loadingProgressBar.visibility = View.GONE
            }
        )
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}