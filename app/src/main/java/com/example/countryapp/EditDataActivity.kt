package com.example.countryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import com.example.countryapp.data.api.DataModel
import com.example.countryapp.data.sqlite.SQLite
import com.example.countryapp.databinding.ActivityEditDataBinding
import com.example.countryapp.databinding.ActivityMainBinding

class EditDataActivity : AppCompatActivity() {
    private val db = SQLite(this)
    private var country = DataModel()
    private lateinit var binding: ActivityEditDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the Intent that started this activity and extract the string
        val position = intent.getStringExtra("posicaoValor")
        if (position != null) {
            getCountryData(position)
        }

        binding.btnSalvar.setOnClickListener {
            updateData()
        }
    }

    private fun updateData() {
        //Get new object
        country.Year = binding.tfAno.text.toString()
        country.State = binding.tfEstado.text.toString()
        country.IDState = binding.tfId.text.toString()
        country.Population = binding.tfPopulacao.text.toString()
        country.SlugState = binding.tfSlug.text.toString()
        //Insert new object
        db.updateCountryData(country)
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun getCountryData(position: String) {
        var selectedCountry: DataModel = db.showSelectedCountry(country, position)
        country.IDYear = selectedCountry.IDYear.toString()
        binding.tfAno.setText(selectedCountry.Year.toString())
        binding.tfEstado.setText(selectedCountry.State.toString())
        binding.tfId.setText(selectedCountry.IDState.toString())
        binding.tfPopulacao.setText(selectedCountry.Population.toString())
        binding.tfSlug.setText(selectedCountry.SlugState.toString())
    }

}