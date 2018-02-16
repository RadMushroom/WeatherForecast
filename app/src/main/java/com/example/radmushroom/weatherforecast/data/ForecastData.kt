package com.example.radmushroom.weatherforecast.data

import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.radmushroom.weatherforecast.controller.AppController
import com.example.radmushroom.weatherforecast.model.Forecast
import org.json.JSONArray
import org.json.JSONObject


class ForecastData {

    var forecastMutableList: MutableList<Forecast> = mutableListOf()
    val leftUrl: String = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"
    val rightUrl = "%22)%20and%20u%3D%22c%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"

    fun getForecast(callback:ForecastListAsyncResponse, locationText: String ) {
        val url:String = leftUrl+locationText+rightUrl
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url,
                Response.Listener<JSONObject> { response ->
                    val query: JSONObject = response.getJSONObject("query")
                    val results: JSONObject = query.getJSONObject("results")
                    val channel: JSONObject = results.getJSONObject("channel")
                    val location: JSONObject = channel.getJSONObject("location")
                    val city: String = location.getString("city")
                    val country: String = location.getString("country")
                    val region: String = location.getString("region")

                    //Item Object
                    val itemObject: JSONObject = channel.getJSONObject("item")
                    val pubDate: String = itemObject.getString("pubDate")

                    //Condition Object
                    val condition: JSONObject = itemObject.getJSONObject("condition")

                    //Forecast JSONArray
                    val forecastArray: JSONArray = itemObject.getJSONArray("forecast")
                    for (i in 0 until forecastArray.length()){

                        val forecast = Forecast()
                        val forecastObject: JSONObject = forecastArray.getJSONObject(i)
                        forecast.forecastDate = forecastObject.getString("date")
                        forecast.forecastDay = forecastObject.getString("day")
                        forecast.forecastHighTemp = forecastObject.getString("high")
                        forecast.forecastLowTemp = forecastObject.getString("low")
                        forecast.forecastWeatherDescription = forecastObject.getString("text")
                        forecast.country = location.getString("country")
                        forecast.city = location.getString("city")
                        forecast.currentTemperature = condition.getString("temp")
                        forecast.currentWeatherDescription = condition.getString("text")
                        forecast.date = condition.getString("date")
                        forecast.descriptionHTML = itemObject.getString("description")
                        forecastMutableList.add(forecast)
                    }
                    callback.processFinished(forecastMutableList)
                },
                Response.ErrorListener {
                    Log.i("error", "That didn`t work")
                })
        AppController.mInstance?.addToRequestQueue(jsonObjectRequest)
    }
}