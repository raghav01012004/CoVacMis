package com.example.covacmis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VaccinationChart : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList:ArrayList<DataClass>
    private lateinit var vaccineName:Array<String>
    private lateinit var ageGroup:Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccination_chart)

        vaccineName = arrayOf(
            "BCG",
            "VARICELLA",
            "ORAL POLIO",
            "HEPATITIS B",
            "TDAP",
        )

        ageGroup = arrayOf(
            "0 - 1",
            "1 - 2",
            "9 - 45",
            "6 - 12",
            "1 - 3",
        )

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<DataClass>()
        getData()

    }



    private fun getData(){
        for(i in 0..4){
            val dataClass = DataClass(vaccineName[i],ageGroup[i])
            dataList.add(dataClass)
        }
        recyclerView.adapter = AdapterClass(dataList)
    }
}