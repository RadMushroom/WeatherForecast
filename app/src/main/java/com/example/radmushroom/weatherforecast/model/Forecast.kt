package com.example.radmushroom.weatherforecast.model

import java.io.Serializable


data class Forecast(var city: String?, var country: String?, var region: String?,
                    //Under Condition Object
                    var date: String?, var currentTemperature: String?,
                    var currentWeatherDescription: String?,
                    //Under Forecast Object
                    var forecastDate: String?, var forecastDay: String?, var forecastHighTemp: String?,
                    var forecastLowTemp: String?, var forecastWeatherDescription: String?,
                    //Description HTML
                    var descriptionHTML: String?) : Serializable {
    constructor(): this("","","","","","",
            "","","","","","")
}

