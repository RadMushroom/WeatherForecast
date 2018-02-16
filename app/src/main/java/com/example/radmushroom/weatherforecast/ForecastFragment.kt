package com.example.radmushroom.weatherforecast


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.radmushroom.weatherforecast.model.Forecast
import kotlinx.android.synthetic.main.fragment_forecast.*

class ForecastFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_forecast, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val forecast: Forecast = arguments.getSerializable("forecast") as Forecast
        Log.e("forecast", forecast.toString())
//        forecastTempText?.text = forecast.currentTemperature
        forecastDateText?.text = forecast.forecastDate
        forecastHighText?.text = forecast.forecastHighTemp
        forecastLowText?.text = forecast.forecastLowTemp
        forecastDescriptionTextView?.text = forecast.forecastWeatherDescription

    }
     fun newInstance(forecast: Forecast): ForecastFragment {
        val forecastFragment = ForecastFragment()
        val bundle = Bundle()
        bundle.putSerializable("forecast", forecast)
        forecastFragment.arguments = bundle
        return forecastFragment
    }

}// Required empty public constructor
