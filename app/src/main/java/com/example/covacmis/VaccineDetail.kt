package com.example.covacmis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class VaccineDetail : AppCompatActivity() {
    private lateinit var apiManager: ApiManager
    private lateinit var heading: TextView
    private lateinit var types: TextView
    private lateinit var live: TextView
    private lateinit var gender: TextView
    private lateinit var age: TextView
    private lateinit var dose: TextView
    private lateinit var company: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine_detail)

        heading = findViewById(R.id.heading)
        types = findViewById(R.id.typedesc)
        live = findViewById(R.id.la)
        gender = findViewById(R.id.gender)
        age = findViewById(R.id.agegrp)
        dose = findViewById(R.id.dosecnt)
        company = findViewById(R.id.companynm)

        val clickedItem = intent.getParcelableExtra<DataClass>("clickedItem")
        apiManager = ApiManager(this)

        if (clickedItem != null) {
            apiManager.getVacObj(clickedItem.dataVaccineName) { vacObj ->
                runOnUiThread {
                    if (vacObj != null) {
                        gender.text = vacObj.gender
                        dose.text = vacObj.dose_count.toString()
                        heading.text = vacObj.name
                        age.text = clickedItem.ageGroup
                        types.text = vacObj.type.joinToString(", ")
                    } else {
                        // Handle error
                    }
                }
            }
        }
    }

    private fun getVaccineDetail(vaccineName:String){
        val url = "http://10.0.2.2:8000/vaccines/$vaccineName"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val vac = Gson().fromJson(response.toString(), VacObj::class.java)
                println(vac)
            },
            { error ->
                Log.d("VaccineDetail", error.toString())
            })

        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}