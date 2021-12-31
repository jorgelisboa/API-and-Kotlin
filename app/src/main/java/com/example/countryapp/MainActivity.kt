package com.example.countryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.countryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateList()
    }

    private fun updateList() {

    }
}