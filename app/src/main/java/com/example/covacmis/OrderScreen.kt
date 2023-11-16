package com.example.covacmis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class OrderScreen : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var hospitalInfo:HospitalLogin
    private lateinit var orderList:ArrayList<Order>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_screen)

        recyclerView = findViewById(R.id.orderRecycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        hospitalInfo = intent.getSerializableExtra("hospitalDetail") as HospitalLogin
        orderList = arrayListOf()
        getOrders(hospitalInfo.hospitalName)
    }


    private fun getOrders(hospitalName:String){
        orderList.clear()
        val url = "http://10.0.2.2:8000/hospital/getOrders/$hospitalName"
        val request = JsonObjectRequest(
            Request.Method.GET,url,null,
            { response->
                for (key in response.keys()){
                    val orderData = response.getJSONObject(key)
                    val order = Order(key,orderData["order"].toString(),orderData["date"].toString())
                    println(order)
                    orderList.add(order)
                }
                val orderAdapter = OrderAdapter(orderList)
                recyclerView.adapter = orderAdapter
            },
            {
               error->
                Log.d("OrderScreen",error.toString())
            }
        )
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}