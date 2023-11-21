package com.example.covacmis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

        val hospitalArray = arrayOf(
            "Fortis",
            "Kailash",
            "AIIMS",
            "SJM",
            "MAX",
            "Jaypee",
            "Evan"
        )

        val hospitalCityArray = arrayOf(
            "Noida",
            "GZB",
            "Rishikesh",
            "GZB",
            "Delhi",
            "Greater Noida",
            "MZN"
        )

        val hospitalDistanceArray = arrayOf(
            "1",
            "16",
            "250",
            "25",
            "40",
            "36",
            "130"
        )

//        val newsContent = arrayOf(
//            getString(R.string.news_content), getString(R.string.news_content),
//            getString(R.string.news_content), getString(R.string.news_content),
//            getString(R.string.news_content), getString(R.string.news_content)
//        )

        // to set hav bhav of items inside recyclerview, vertcially scrolling, horizontally, uniform grid
        myRecyclerView.layoutManager = LinearLayoutManager(this)
        hospitalArrayList = arrayListOf<Hospital>()

        for( index in hospitalArray.indices){
            val hospital = Hospital(hospitalArray[index], hospitalCityArray[index], hospitalDistanceArray[index])
            hospitalArrayList.add(hospital)
        }

        val myAdapter = MyAdapter(hospitalArrayList, this)
        myRecyclerView.adapter = myAdapter

        myAdapter.setOnItemClickListener(object : MyAdapter.onItemClickListener {
            override fun onItemClicking(position: Int) {
                // on clicking each item , what action do you want to perform

//                val intent = Intent(this@hospitalList, NewsDetailActivity::class.java)
//                intent.putExtra("heading", newsArrayList[position].newsHeading)
//                intent.putExtra("imageId", newsArrayList[position].newsImage)
//                intent.putExtra("newscontent", newsArrayList[position].newsContent)
//                startActivity(intent)
            }

        })
    }
}