package com.example.countryapp

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.countryapp.data.api.DataModel
import com.example.countryapp.data.sqlite.SQLite
import com.example.countryapp.databinding.ActivityMainBinding
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private val url = "https://datausa.io/api/data?drilldowns=State&measures=Population&year=latest"
    private lateinit var binding: ActivityMainBinding
    private var mQueue: RequestQueue? = null
    private val country = DataModel()
    private val db = SQLite(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mQueue = Volley.newRequestQueue(this)

        jsonParse()
        binding.lvPaises.setOnItemClickListener {
                data, _, position, _ ->

            findCountry(data.getItemAtPosition(position) as String)
        }
    }

    private fun getStateString(value: String): String {
        var newValue: String = ""
        newValue = value.replace(1.toString(), "")
        newValue = newValue.replace(2.toString(), "")
        newValue = newValue.replace(3.toString(), "")
        newValue = newValue.replace(4.toString(), "")
        newValue = newValue.replace(5.toString(), "")
        newValue = newValue.replace(6.toString(), "")
        newValue = newValue.replace(7.toString(), "")
        newValue = newValue.replace(8.toString(), "")
        newValue = newValue.replace(9.toString(), "")
        newValue = newValue.replace(0.toString(), "")
        newValue = newValue.replace(" ", "")
        return newValue
    }

    private fun findCountry(state: String) {
        val intent = Intent(this, EditDataActivity::class.java).apply {
            putExtra("posicaoValor", getStateString(state))
        }
        startActivity(intent)
    }

    private fun jsonParse(){
        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, {
            //For success
            val jsonArray = it.getJSONArray("data")

            for (i in 0 until jsonArray.length()){
                val data = jsonArray.getJSONObject(i)

                country.IDState = data.getString("ID State")
                country.IDYear = data.getString("ID Year")
                country.Population = data.getString("Population")
                country.State = data.getString("State")
                country.Year = data.getString("Year")
                country.SlugState = data.getString("Slug State")

                db.addCountry(country)
            }
            updateList()
        }, {
            //For fail

            Toast.makeText(this, "ERRO DE CONEXÃO", Toast.LENGTH_SHORT).show()
            Log.d("FAILURE", "ERRO")
        })
        queue.add(jsonObjectRequest)

    }

    private fun updateList() {
        var adapter: ArrayAdapter<String>? = ArrayAdapter<String>(this, R.layout.simple_list_item_1, db.showAll())
        binding.lvPaises.adapter = adapter
    }
}

