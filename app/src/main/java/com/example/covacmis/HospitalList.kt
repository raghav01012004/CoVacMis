package com.example.covacmis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private lateinit var myRecyclerView : RecyclerView
    private lateinit var hospitalArrayList: ArrayList<Hospital>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_hospital)

        supportActionBar?.hide()
        myRecyclerView = findViewById(R.id.recyclerView2)

        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.setHasFixedSize(true)
        hospitalArrayList = arrayListOf()

        getHospitalData()

    }

    private fun getHospitalData(){
        val url = "http://10.0.2.2:8000/hospital/getData"
        val request = JsonObjectRequest(
            Request.Method.GET,url,null,
            {
                response->
                    println(response)
                    for(det in response.keys()){
                        val hosObj = response.getJSONObject(det)
                        val locArray = hosObj.getJSONArray("location")
                        val address = hosObj["address"]
                        val hospitalName = hosObj["hospitalName"]
                        val createHospitalDetailObject = Hospital(hospitalName.toString(),address.toString(),"40")
                        hospitalArrayList.add(createHospitalDetailObject)
                    }
                val myAdapter = MyAdapter(hospitalArrayList,this)
                myRecyclerView.adapter = myAdapter

                myAdapter.setOnItemClickListener(object : MyAdapter.onItemClickListener {
                    override fun onItemClicking(position: Int) {
                    }
                })

            },
            {
                error->
                    Log.d("HospitalList",error.toString())
            }
        )
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}