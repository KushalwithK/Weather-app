package com.practice.weatherapiapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.practice.weatherapiapp.WeatherLoaderClass.VolleyRequestCallback

class MainActivity : AppCompatActivity() {

    // All the imageview from our XML layout
    private lateinit var weatherIcon: ImageView

    // All the buttons from our XML layout
    private lateinit var btn_getWeatherByName: Button

    // All the edit texts from our XML Layout
    private lateinit var et_cityNameOrIdInput: EditText

    // All the textViews from our XML layout
    private lateinit var tv_cityName: TextView
    private lateinit var tv_condition: TextView
    private lateinit var tv_dayNight: TextView
    private lateinit var tv_tempCelsius: TextView
    private lateinit var tv_tempFahrenheit: TextView
    private lateinit var tv_currentWeatherCity: TextView

    // All the progressBar from our XML layout
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_getWeatherByName = findViewById(R.id.btn_getWeatherByCityName)
        et_cityNameOrIdInput = findViewById(R.id.et_cityNameOrIdInput)
        tv_currentWeatherCity = findViewById(R.id.tv_currentWeatherCity)

        tv_cityName = findViewById(R.id.tv_cityName)
        tv_condition = findViewById(R.id.tv_condition)
        tv_dayNight = findViewById(R.id.tv_dayNight)
        tv_tempCelsius = findViewById(R.id.tv_tempCelsius)
        tv_tempFahrenheit = findViewById(R.id.tv_tempFahrenheit)

        weatherIcon = findViewById(R.id.weatherIcon)

        progressBar = findViewById(R.id.progressBar)

        supportActionBar?.hide()

        btn_getWeatherByName.setOnClickListener {

            val weatherLoader = WeatherLoaderClass(this)
            if (et_cityNameOrIdInput.text.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                weatherLoader.loadWeatherByName(et_cityNameOrIdInput.text.toString(), object: VolleyRequestCallback {
                    override fun onResponse(
                        cityName: String,
                        tempCelsius: String,
                        tempFahrenheit: String,
                        isDay: Boolean,
                        condition: String,
                        iconUrl: String,
                        context: Context
                    ) {
                        tv_cityName.text = cityName
                        tv_condition.text = condition
                        tv_dayNight.text = if (isDay) "Day" else "Night"
                        tv_tempCelsius.text = tempCelsius
                        tv_tempFahrenheit.text = tempFahrenheit
                        Glide.with(context).load(iconUrl).into(weatherIcon)
                        tv_currentWeatherCity.text = "The Current Weather is For $cityName"

                        progressBar.visibility = View.GONE
                    }

                    override fun onFailed(error: String) {
                        progressBar.visibility = View.GONE
                        tv_cityName.text = "- - - -"
                        tv_condition.text = "- - - - - - - - - -"
                        tv_dayNight.text = "--"
                        tv_tempCelsius.text = "--"
                        tv_tempFahrenheit.text = "--"
                        tv_currentWeatherCity.text = "The City might not exist or some error occurred.."
                    }

                }, progressBar)
            } else {
                Toast.makeText(this, "Please enter a city name above!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}