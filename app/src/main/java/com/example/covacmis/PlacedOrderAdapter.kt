package com.example.covacmis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class PlacedOrderAdapter(private val placedOrderList:ArrayList<PlacedOrder>):RecyclerView.Adapter<PlacedOrderAdapter.ViewHolderClass>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.placed_order_item,parent,false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: PlacedOrderAdapter.ViewHolderClass, position: Int) {
        val currentItem = placedOrderList[position]
        holder.orderVacName.text = currentItem.vaccine
        holder.orderHosName.text = currentItem.hospital
        holder.orderDate.text = currentItem.date
        holder.orderBrand.text = currentItem.brand

    }

    override fun getItemCount(): Int {
        return placedOrderList.size
    }

    inner class ViewHolderClass(itemView:View):RecyclerView.ViewHolder(itemView){
        val orderVacName:TextView = itemView.findViewById(R.id.orderVac)
        val orderHosName:TextView = itemView.findViewById(R.id.orderHosName)
        val orderDate:TextView = itemView.findViewById(R.id.orderHosDate)
        val orderBrand:TextView = itemView.findViewById(R.id.placedBrand)
    }
}