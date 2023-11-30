package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import kotlin.math.*

class HospitalList : AppCompatActivity(), MyAdapter.OnItemClickListener {

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
    private var selectedHospital: String = ""
    private lateinit var listOverlay: FrameLayout
    private var address: String = ""
    private var latitude: String = ""
    private var longitude: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_hospital)
        val brandName = intent.getStringExtra("brandName")
        val vaccine = intent.getStringExtra("vaccine")
        val userdetail = intent.getSerializableExtra("userdetail") as User
        val companyName = intent.getStringExtra("companyName")
        val date = intent.getStringExtra("selectedDate")
        latitude = intent.getStringExtra("latitude").toString()
        longitude = intent.getStringExtra("longitude").toString()
        address = intent.getStringExtra("address").toString()

        orderVac = findViewById(R.id.orderVacName)
        orderBrand = findViewById(R.id.orderBrand)
        orderDate = findViewById(R.id.orderDate)
        orderButton = findViewById(R.id.orderPlaceButton)
        listOverlay = findViewById(R.id.ListOverlay)

        orderVac.text = vaccine
        orderBrand.text = brandName
        orderDate.text = date


        supportActionBar?.hide()
        myRecyclerView = findViewById(R.id.recyclerView2)

        myRecyclerView.layoutManager = LinearLayoutManager(this)
        myRecyclerView.setHasFixedSize(true)
        hospitalArrayList = arrayListOf()

        getHospitalData()

        orderButton.setOnClickListener {
            listOverlay.visibility = View.VISIBLE
            orderButton.isEnabled = false
            val url = "https://covacmis.onrender.com/user/order/"
            val requestBody = JSONObject().apply {
                put("username", userdetail.username)
                put("vaccine_name", vaccine)
                put("date", date)
                put("hospital_name", selectedHospital)
                put("brand_name", brandName)
                put("company_name", companyName)
            }
            val request = JsonObjectRequest(
                Request.Method.POST, url, requestBody,
                { response ->
                    if (response["success"] == 0) {
                        Toast.makeText(
                            this,
                            "${response["message"]}. Check your orders",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this, "${response["message"]}", Toast.LENGTH_SHORT).show()
                    }
                    listOverlay.visibility = View.GONE
                    orderButton.isEnabled = true
                    val intent = Intent(this, VaccinationChart::class.java)
                    intent.putExtra("user", userdetail)
                    startActivity(intent)
                },
                { error ->
                    Log.d("HospitalList", error.toString())
                    listOverlay.visibility = View.GONE
                }
            )
            val queue = Volley.newRequestQueue(this)
            queue.add(request)

        }
    }

    private fun getHospitalData() {
        listOverlay.visibility = View.VISIBLE
        val url = "https://covacmis.onrender.com/hospital/getData"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                println(response)
                for (det in response.keys()) {
                    val hosObj = response.getJSONObject(det)
                    val locArray = hosObj.getJSONArray("location")
                    val lat2 = locArray[0].toString()
                    val lon2 = locArray[1].toString()
                    val addr = hosObj["address"]
                    val hospitalName = hosObj["hospitalName"]
                    val distance = haversine(
                        latitude.toDouble(),
                        longitude.toDouble(),
                        lat2.toDouble(),
                        lon2.toDouble()
                    )
                    val createHospitalDetailObject =
                        Hospital(
                            det,
                            hospitalName.toString(),
                            addr.toString(), distance.toString(),
                            locArray[0].toString(),
                            locArray[1].toString()
                        )
                    if (address == addr.toString()) {
                        hospitalArrayList.add(createHospitalDetailObject)
                    }
                }
                val myAdapter = MyAdapter(hospitalArrayList, this, listOverlay)
                myRecyclerView.adapter = myAdapter
                listOverlay.visibility = View.GONE
            },
            { error ->
                Log.d("HospitalList", error.toString())
                listOverlay.visibility = View.GONE
            }
        )
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

    override fun onHospitalSelected(hospitalName: String) {
        selectedHospital = hospitalName
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
        val r = 6371

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return (r * c).toInt()
    }
}