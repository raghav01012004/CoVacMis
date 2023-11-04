package com.example.covacmis

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import java.net.URL
import java.net.URLEncoder

class VaccineDetail : AppCompatActivity() {
    private lateinit var apiManager: ApiManager
    private lateinit var heading: TextView
    private lateinit var types: TextView
    private lateinit var live: TextView
    private lateinit var gender: TextView
    private lateinit var age: TextView
    private lateinit var dose: TextView
    private lateinit var company: TextView
    private lateinit var companyList : List<String>
    private lateinit var companyDataList: ArrayList<SetCompanyData>
    private lateinit var companyRecyclerView: RecyclerView

    @SuppressLint("SetTextI18n")
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
        companyDataList = ArrayList()

        companyRecyclerView = findViewById(R.id.companyRecycler)
        companyRecyclerView.layoutManager = LinearLayoutManager(this)
        companyRecyclerView.setHasFixedSize(true)

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
                        companyList = vacObj.companies
                        company.text = vacObj.companies.joinToString(",\n")
                        if (vacObj.live_attenuated) {
                            live.text = "Yes"
                        } else {
                            live.text = "No"
                        }
                        println(companyList)
                        for (i in companyList) {
                            getCompanyData(i, clickedItem.dataVaccineName)
                        }

                    } else {
                        // Handle error
                    }
                }
            }
        }


    }
    private fun getCompanyData(companyName: String, vaccineName: String) {
        val url = "http://10.0.2.2:8000/price/$companyName/$vaccineName"
        println(url)

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val company = Gson().fromJson(response.toString(), CompanyData::class.java)
                val priceStr = "₹ ${company[1].toString()}"
                val companyObj: SetCompanyData = SetCompanyData(companyName, company[0].toString(), priceStr)
                println(companyObj)
                companyDataList.add(companyObj)

                // Check if all data has been collected before setting up the adapter
                if (companyDataList.size == companyList.size) {
                    println(companyDataList)
                    val myCompanyAdapter = CompanyAdapter(companyDataList)
                    companyRecyclerView.adapter = myCompanyAdapter
                }
            },
            { error ->
                Log.d("VaccinationDetail", error.toString())
            })

        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

}