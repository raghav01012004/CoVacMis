package com.example.covacmis

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class ApiManager(private val context: Context) {

    private val queue: RequestQueue = Volley.newRequestQueue(context)
    private val gson: Gson = Gson()

    fun getVacObj(vaccineName: String, callback: (VacObj?) -> Unit) {
        val url = "https://covacmis.onrender.com/vaccines/$vaccineName"

        val request = StringRequest(Request.Method.GET, url,
            { response ->
                val vacObj = gson.fromJson(response, VacObj::class.java)
                callback(vacObj)
            },
            { error ->
                error.printStackTrace()
                callback(null)
            })

        queue.add(request)
    }
}

