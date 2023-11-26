package com.example.covacmis

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class CompanyAdapter(private val companyDataList:ArrayList<SetCompanyData>,private val vaccineName:String,private val userDetail:User,private val locationFetchListener: LocationFetchListener):RecyclerView.Adapter<CompanyAdapter.ViewHolderClass>() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var address: String
    private lateinit var itemView: View
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
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(itemView.context)
                fetchLocation(object : LocationFetchListener {
                    override fun onLocationFetchCompleted(
                        latitude: String,
                        longitude: String,
                        address: String
                    ) {
                        val brandName = clickedItem.brandName
                        val vaccine = vaccineName
                        val username = userDetail.username
                        val companyName = clickedItem.companyName

                        // Show the date picker dialog
                        val datePickerFragment = DatePickerFragment()
                        val args = Bundle()
                        args.putString("brandName", brandName)
                        args.putString("vaccine", vaccine)
                        args.putString("username", username)
                        args.putString("companyName", companyName)
                        args.putString("latitude",latitude)
                        args.putString("longitude",longitude)
                        args.putString("address",address)
                        datePickerFragment.arguments = args

                        datePickerFragment.show((itemView.context as AppCompatActivity).supportFragmentManager, "datePicker")

                    }
                })

                //TODO(FETCH USER LATITUDE LONGITUDE AND ADDRESS(CITY) AFTER FETCHING PASS THE ABOVE 4 DETAILS(brandName,vaccine,username,companyName) + (latitude,longitude and address) to the next screen i.e. hospital list


//                itemView.context.startActivity(Intent(itemView.context,HospitalList::class.java))
             }
        }

    }
    private fun fetchLocation(locationFetchListener: LocationFetchListener)
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
                val textLatitude = it.latitude.toString()
                val textLongitude = it.longitude.toString()
                val textaddress = getAddressName(it.latitude, it.longitude)
                //Toast.makeText(itemView.context, "Latitude is : ${it.latitude} \n AND \n Longitude is : ${it.longitude}", Toast.LENGTH_SHORT).show()
                Toast.makeText(itemView.context, textaddress, Toast.LENGTH_SHORT).show()
                latitude = textLatitude
                longitude = textLongitude
                address = textaddress
                locationFetchListener.onLocationFetchCompleted(latitude, longitude, address)
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