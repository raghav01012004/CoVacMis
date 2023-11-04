package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray

class VaccinationChart : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    private lateinit var vaccineName: ArrayList<String>
    private lateinit var ageGroup: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccination_chart)

        vaccineName = arrayListOf()

        ageGroup = arrayListOf()

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<DataClass>()
        getVaccines()
    }


    private fun getVaccines() {
        val url = "http://10.0.2.2:8000/vaccines"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                for (key in response.keys()) {
                    val vaccineData = response.getJSONObject(key)
                    val ageGroupList = vaccineData.optJSONArray("Age_group")

                    val ageGroupString = when {
                        ageGroupList == null -> "any"
                        ageGroupList.length() == 1 -> ">${ageGroupList.getDouble(0)}"
                        ageGroupList.getDouble(0) == ageGroupList.getDouble(1) -> "${ageGroupList.getDouble(0)}"
                        else -> "${ageGroupList.getDouble(0)} - ${ageGroupList.getDouble(1)}"
                    }

                    val doseCount = vaccineData.optInt("dose_count")
                    val doseCountString = doseCount.toString()

                    val dataClass = DataClass(key, ageGroupString, doseCountString)
                    dataList.add(dataClass)
                }

                val myAdapter = AdapterClass(dataList)
                recyclerView.adapter = myAdapter

                // Now you have a list of DataClass objects
                // You can do whatever you want with this list here
            },
            { error ->
                Log.d("VaccinationChart", error.toString())
            })

        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

}
