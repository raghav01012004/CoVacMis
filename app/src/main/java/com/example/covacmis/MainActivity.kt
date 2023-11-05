package com.example.covacmis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val textView = findViewById<TextView>(R.id.textView4)
        val username = findViewById<EditText>(R.id.editText)
        val password = findViewById<EditText>(R.id.editText2)
        val button = findViewById<Button>(R.id.button)



        button.setOnClickListener {
            val uname = username.text?.toString()
            val pass = password.text?.toString()

            if (uname.isNullOrEmpty() || pass.isNullOrEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please fill the above fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                getData(uname, pass)
            }

        }
        textView.setOnClickListener {
            Toast.makeText(applicationContext, "Wait a sec...", Toast.LENGTH_SHORT).show()

            intent = Intent(applicationContext, MainActivity2::class.java)
            startActivity(intent)
        }


    }

    private fun getData(username: String, password: String) {
        // Assuming you have `username` and `password` as Strings
        val url = "https://covacmis.onrender.com/login/$username/$password"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                val user = Gson().fromJson(response.toString(), User::class.java)
//                for(i in response.keys()){
//                    println(response[i])
//                }
                if (user?.fullname != null && user.fullname.isNotEmpty()) {
                    val intent = Intent(applicationContext, VaccinationChart::class.java).putExtra("user",user)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            { error ->
                Log.d("MainActivity", error.toString())
            })
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

}
