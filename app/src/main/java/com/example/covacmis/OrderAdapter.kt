package com.example.covacmis

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.ArrayList

class OrderAdapter(
    private val orderList: ArrayList<Order>,
    private val hospitalDetail: HospitalLogin,
    private val recyclerView: RecyclerView,
    private val noOrdersTextView: TextView
) :
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
        if (orderList.isEmpty()) {
            recyclerView.visibility = View.GONE
            noOrdersTextView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            noOrdersTextView.visibility = View.GONE
        }
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
        private val comButton: ImageButton = itemView.findViewById(R.id.removeButton)

        init {
            comButton.setOnClickListener {
                val position = adapterPosition
                val clickedItem = orderList[position]
                val url = "https://covacmis.onrender.com/hospital/orderComplete"

                val requestBody = JSONObject().apply {
                    put("hospitalName", hospitalDetail.hospitalName)
                    put("date", date.text.toString())
                    put("username", userName.text.toString())
                }

                val request = JsonObjectRequest(
                    Request.Method.POST, url, requestBody,
                    { response ->
                        if (response["success"] == 1) {
                            orderList.removeAt(position)
                            notifyDataSetChanged()
                            Toast.makeText(
                                itemView.context,
                                "Order Completed!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                itemView.context,
                                "${response["message"]}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        orderList.remove(clickedItem)
                    },
                    { error ->
                        Log.d("OrderAdapter", error.toString())
                    }
                )
                val queue = Volley.newRequestQueue(itemView.context)
                queue.add(request)

            }
        }
    }

}
