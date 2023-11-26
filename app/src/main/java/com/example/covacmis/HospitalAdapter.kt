package com.example.covacmis

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class MyAdapter(private var hospitalArrayList: ArrayList<Hospital>, var context : Activity) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    private lateinit var myListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClicking(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener){
        myListener = listener
    }

    // to create new view instance
    // when layout manager fails to find a suitable view for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hospital_layout, parent, false)
        return MyViewHolder(itemView, myListener)
    }

    // populate items with data
    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val currentItem = hospitalArrayList[position]
        holder.hTitle.text = currentItem.fullname
        holder.hCity.text = currentItem.city
        holder.hDistance.text = currentItem.distance.toString()
    }


    // how many list items are present in your array
    override fun getItemCount(): Int {
        return hospitalArrayList.size
    }

    // it holds the view so views are not created everytime, so memory can be saved
    inner class MyViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val hTitle:TextView = itemView.findViewById(R.id.textViewHospitalName)
        val hCity:TextView = itemView.findViewById(R.id.textViewCity)
        val hDistance:TextView = itemView.findViewById(R.id.textViewDistance)
        private val mapBtn:ImageButton = itemView.findViewById(R.id.imageButton)

        init {
            itemView.setOnClickListener {
                listener.onItemClicking(adapterPosition)
            }
            mapBtn.setOnClickListener{
                val position = adapterPosition
                val clickedItem = hospitalArrayList[position]
                val latitude = clickedItem.latitude.toDouble()
                val longitude = clickedItem.longitude.toDouble()
                val intent = Intent(itemView.context,gMap::class.java)
                intent.putExtra("latitude",latitude)
                intent.putExtra("longitude",longitude)
                itemView.context.startActivity(intent)
            }
        }
    }

}