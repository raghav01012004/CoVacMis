package com.example.covacmis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CompleteAdapter(private val completedList:ArrayList<CompletedVac>):RecyclerView.Adapter<CompleteAdapter.ViewHolderClass>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.completed_layout,parent,false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: CompleteAdapter.ViewHolderClass, position: Int) {
        val currentItem = completedList[position]
        holder.comVacName.text = currentItem.vaccine
        holder.comCount.text = currentItem.count
        holder.comVacDate.text = currentItem.date

    }

    override fun getItemCount(): Int {
        return completedList.size
    }

    inner class ViewHolderClass(itemView:View):RecyclerView.ViewHolder(itemView){
        val comVacName:TextView = itemView.findViewById(R.id.comVac)
        val comVacDate:TextView = itemView.findViewById(R.id.comLastDate)
        val comCount :TextView = itemView.findViewById(R.id.comCount)
    }
}