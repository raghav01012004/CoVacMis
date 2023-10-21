package com.example.covacmis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterClass(private val dataList:ArrayList<DataClass>):RecyclerView.Adapter<AdapterClass.ViewHolderClass>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvAge.text = currentItem.ageGroup
        holder.rvVaccine.text = currentItem.dataVaccineName

    }


    class ViewHolderClass(itemView: View):RecyclerView.ViewHolder(itemView) {
        val rvVaccine:TextView = itemView.findViewById(R.id.VaccineName)
        val rvAge:TextView = itemView.findViewById(R.id.Age)

    }
}