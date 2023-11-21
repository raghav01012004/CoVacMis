package com.example.covacmis

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class CompanyAdapter(private val companyDataList:ArrayList<SetCompanyData>,private val vaccineName:String,private val userDetail:User):RecyclerView.Adapter<CompanyAdapter.ViewHolderClass>() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var address: String
    lateinit var itemView: View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.company_layout,parent,false)
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

                itemView.context.startActivity(Intent(itemView.context,HospitalList::class.java))

                //TODO(FETCH USER LATITUDE LONGITUDE AND ADDRESS(CITY) AFTER FETCHING PASS THE ABOVE 4 DETAILS(brandName,vaccine,username,companyName) + (latitude,longitude and address) to the next screen i.e. hospital list
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(itemView.context)
                fetchLocation()
             }
        }

    }
    private fun fetchLocation()
    {
        val task = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(itemView.context,android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(itemView.context,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(itemView.context as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),101)
            return
        }
        task.addOnSuccessListener {
            if(it != null){
                val textLatitude = "Latitude : " + it.latitude.toString()
                val textLongitude = "Longitude : " + it.longitude.toString()
                val textaddress = "Address : " + getAddressName(it.latitude, it.longitude)
                //Toast.makeText(itemView.context, "Latitude is : ${it.latitude} \n AND \n Longitude is : ${it.longitude}", Toast.LENGTH_SHORT).show()
                Toast.makeText(itemView.context, "$textaddress", Toast.LENGTH_SHORT).show()
                latitude = textLatitude
                longitude = textLongitude
                address = textaddress
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getAddressName(lat: Double, lon:Double): String{

        var addressName = ""
        val geoCoder = Geocoder(itemView.context, Locale.getDefault())
        val address = geoCoder.getFromLocation(lat,lon,1)

        if (address != null) {
            addressName = address[0].locality
        }
        return addressName
    }


}