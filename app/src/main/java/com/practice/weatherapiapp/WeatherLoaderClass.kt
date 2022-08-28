package com.practice.weatherapiapp

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest

class WeatherLoaderClass(val ctx: Context) {

    public interface VolleyRequestCallback {
        public fun onResponse(
            cityName: String, tempCelsius: String, tempFahrenheit: String, isDay: Boolean, condition: String, iconUrl: String, context: Context
        )
        public fun onFailed(error: String)
    }

    val QUERY_URL = "https://api.weatherapi.com/v1/current.json?key=ef0732ed928d41d18be122441222708&q="

    fun loadWeatherByName(city: String, volleyRequestCallback: VolleyRequestCallback, progressBar: ProgressBar) {
        val url =
            "$QUERY_URL$city"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                val locationObject = it.getJSONObject("location")
                val innerObject = it.getJSONObject("current")
                val cityName = locationObject.getString("name")
                val tempCelsius = innerObject.getString("temp_c")
                val tempFahrenheit = innerObject.getString("temp_f")
                val isDay: Boolean = innerObject.getString("is_day") == "1"

                val conditionObject = innerObject.getJSONObject("condition")
                val condition = conditionObject.getString("text")

                Toast.makeText(ctx, "The temperature at $cityName" +
                        " is $tempCelsius°C " +
                        "or $tempFahrenheit°F " +
                        "and day is $isDay " +
                        "with condition of $condition", Toast.LENGTH_LONG).show()

                volleyRequestCallback.onResponse(
                    cityName = cityName,
                    tempCelsius = tempCelsius,
                    tempFahrenheit = tempFahrenheit,
                    isDay = isDay,
                    condition = condition,
                    iconUrl = conditionObject.getString("icon"),
                    context = ctx
                )

            }, {
                Log.d("error", it.message.toString())
                volleyRequestCallback.onFailed(it.message.toString())
                Toast.makeText(ctx, "Some error occurred!", Toast.LENGTH_LONG).show()
            })

        MySingleton.getInstance(ctx).addToRequestQueue(jsonObjectRequest)
    }
}