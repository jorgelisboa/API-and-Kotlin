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
    var country = DataModel()
    private lateinit var binding: ActivityEditDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the Intent that started this activity and extract the string
        val position = intent.getIntExtra("posicaoValor", 1)
        getCountryData(position)

        binding.btnSalvar.setOnClickListener {
            updateData()
        }
    }

    private fun updateData() {
        binding.tfAno.text
        binding.tfEstado.text
        binding.tfId.text
        binding.tfPopulacao.text
        binding.tfSlug.text

        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun getCountryData(position: Int) {
        Log.d("JOJI", "String: $position")
        var selectedCountry: DataModel = db.showSelectedCountry(country, position)
        binding.tfAno.setText(selectedCountry.Year.toString())
        binding.tfEstado.setText(selectedCountry.State.toString())
        binding.tfId.setText(selectedCountry.IDState.toString())
        binding.tfPopulacao.setText(selectedCountry.Population.toString())
        binding.tfSlug.setText(selectedCountry.SlugState.toString())
    }


}