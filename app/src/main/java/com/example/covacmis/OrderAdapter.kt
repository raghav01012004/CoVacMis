package com.example.covacmis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class OrderAdapter(private val orderList: ArrayList<Order>) :
    RecyclerView.Adapter<OrderAdapter.ViewHolderClass>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderClass {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.orderitem, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrderAdapter.ViewHolderClass, position: Int) {
        val currentItem = orderList[position]
        holder.userName.text = currentItem.userName
        holder.vacName.text = currentItem.vaccine
        holder.date.text = currentItem.date
    }

    inner class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.userName)
        val vacName: TextView = itemView.findViewById(R.id.vaccineName)
        val date: TextView = itemView.findViewById(R.id.Date)
    }
}