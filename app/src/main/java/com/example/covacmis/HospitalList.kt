package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class HospitalList : AppCompatActivity() {

    //    private lateinit var recyclerView: RecyclerView
//    private lateinit var dataList: ArrayList<DataClass>
//    private lateinit var HospitalName: ArrayList<String>
//    private lateinit var ageGroup: ArrayList<String>
//    private lateinit var overlayContainer: FrameLayout
//
//    private lateinit var userInfo:User
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var hospitalArrayList: ArrayList<Hospital>
    private lateinit var orderVac: TextView
    private lateinit var orderBrand: TextView
    private lateinit var orderDate: TextView
    private lateinit var orderButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_hospital)
        val brandName = intent.getStringExtra("brandName")
        val vaccine = intent.getStringExtra("vaccine")
        val userName = intent.getStringExtra("username")
        val companyName = intent.getStringExtra("companyName")
        val date = intent.getStringExtra("selectedDate")
        val latitude = intent.getStringExtra("latitude")
        val longitude = intent.getStringExtra("longitude")
        val address = intent.getStringExtra("address")

        orderVac = findViewById(R.id.orderVacName)
        orderBrand = findViewById(R.id.orderBrand)
        orderDate = findViewById(R.id.orderDate)
        orderButton = findViewById(R.id.orderPlaceButton)

        orderVac.text = vaccine
        orderBrand.text = brandName
        orderDate.text = date

//        println("$brandName $vaccine $userName $companyName $date $latitude $longitude $address")

        supportActionBar?.hide()
        myRecyclerView = findViewById(R.id.recyclerView2)

        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.setHasFixedSize(true)
        hospitalArrayList = arrayListOf()

        getHospitalData()

        orderButton.setOnClickListener(){

        }

    }

    private fun getHospitalData() {
        val url = "http://10.0.2.2:8000/hospital/getData"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                println(response)
                for (det in response.keys()) {
                    val hosObj = response.getJSONObject(det)
                    val locArray = hosObj.getJSONArray("location")
                    val address = hosObj["address"]
                    val hospitalName = hosObj["hospitalName"]
                    val createHospitalDetailObject =
                        Hospital(hospitalName.toString(), address.toString(), "40",locArray[0].toString(),locArray[1].toString())
                    hospitalArrayList.add(createHospitalDetailObject)
                }
                val myAdapter = MyAdapter(hospitalArrayList, this)
                myRecyclerView.adapter = myAdapter

                myAdapter.setOnItemClickListener(object : MyAdapter.onItemClickListener {
                    override fun onItemClicking(position: Int) {
                    }
                })
            },
            { error ->
                Log.d("HospitalList", error.toString())
            }
        )
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}