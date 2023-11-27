package com.example.covacmis

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private var hospitalArrayList: ArrayList<Hospital>,private var listener:OnItemClickListener,private var listOverlay:FrameLayout) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    private var selectedPosition = RecyclerView.NO_POSITION

    interface OnItemClickListener {
//        fun onItemClicking(position: Int)
        fun onHospitalSelected(hospitalName: String)
    }

    // to create new view instance
    // when layout manager fails to find a suitable view for each item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hospital_layout, parent, false)
        return MyViewHolder(itemView,listener)
    }

    // populate items with data
    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val currentItem = hospitalArrayList[position]
//        holder.radioButton.isChecked = currentItem.isSelected
        holder.bind(currentItem)
        holder.hTitle.text = currentItem.fullname
        holder.hCity.text = currentItem.city
        holder.hDistance.text = currentItem.distance.toString()

    }


    // how many list items are present in your array
    override fun getItemCount(): Int {
        return hospitalArrayList.size
    }

    // it holds the view so views are not created everytime, so memory can be saved
    inner class MyViewHolder(itemView : View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView){
        val hTitle:TextView = itemView.findViewById(R.id.textViewHospitalName)
        val hCity:TextView = itemView.findViewById(R.id.textViewCity)
        val hDistance:TextView = itemView.findViewById(R.id.textViewDistance)
        private val mapBtn:ImageButton = itemView.findViewById(R.id.imageButton)
        val radioButton:RadioButton = itemView.findViewById(R.id.radioButton)

        init {
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
        fun bind(hospital: Hospital) {
            hTitle.text = hospital.fullname
            hCity.text = hospital.city
            hDistance.text = hospital.distance.toString()
            radioButton.isChecked = hospital.isSelected

            radioButton.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    hospitalArrayList.forEachIndexed { index, hospital ->
                        hospital.isSelected = index == currentPosition
                    }
                    updateUI()
                    val clickedItem = hospitalArrayList[currentPosition]
                    listener.onHospitalSelected(clickedItem.fullname)
                }
            }
        }

        private fun updateUI() {
            for (i in hospitalArrayList.indices) {
                notifyItemChanged(i)
            }
        }
    }

}