package com.example.radmushroom.weatherforecast.data

import com.example.radmushroom.weatherforecast.model.Forecast


interface ForecastListAsyncResponse {
    fun processFinished(forecastMutableList: MutableList<Forecast>)
}