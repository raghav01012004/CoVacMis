package com.example.covacmis

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompanyAdapter(private val companyDataList:ArrayList<SetCompanyData>,private val vaccineName:String,private val userDetail:User):RecyclerView.Adapter<CompanyAdapter.ViewHolderClass>() {
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


    inner class ViewHolderClass(itemView: View):RecyclerView.ViewHolder(itemView) {
        val rvCompanyName:TextView = itemView.findViewById(R.id.CompanyName)
        val rvBrandName:TextView = itemView.findViewById(R.id.brandName)
        val rvPrice:TextView = itemView.findViewById(R.id.price)
        private val rvButton:ImageButton = itemView.findViewById(R.id.placeOrder)

        init {
            rvButton.setOnClickListener {
                val position = adapterPosition
                val clickedItem = companyDataList[position]

                val brandName = clickedItem.brandName
                val vaccine = vaccineName
                val username = userDetail.username
                val companyName = clickedItem.companyName

                //TODO(FETCH USER LATITUDE LONGITUDE AND ADDRESS(CITY) AFTER FETCHING PASS THE ABOVE 4 DETAILS(brandName,vaccine,username,companyName) + (latitude,longitude and address) to the next screen i.e. hospital list
             }
        }

    }
}