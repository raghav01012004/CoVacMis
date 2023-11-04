package com.example.covacmis

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompanyAdapter(private val companyDataList:ArrayList<SetCompanyData>):RecyclerView.Adapter<CompanyAdapter.ViewHolderClass>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.company_layout,parent,false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return companyDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = companyDataList[position]
        holder.rvCompanyName.text = currentItem.companyName
        holder.rvBrandName.text = currentItem.brandName
        holder.rvPrice.text = currentItem.price
    }


    class ViewHolderClass(itemView: View):RecyclerView.ViewHolder(itemView) {
        val rvCompanyName:TextView = itemView.findViewById(R.id.CompanyName)
        val rvBrandName:TextView = itemView.findViewById(R.id.brandName)
        val rvPrice:TextView = itemView.findViewById(R.id.price)

    }
}