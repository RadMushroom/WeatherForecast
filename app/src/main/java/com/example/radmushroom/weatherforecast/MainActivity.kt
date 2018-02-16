package com.example.radmushroom.weatherforecast

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.widget.Toast
import com.example.radmushroom.weatherforecast.data.ForecastData
import com.example.radmushroom.weatherforecast.data.ForecastListAsyncResponse
import com.example.radmushroom.weatherforecast.data.ForecastViewPagerAdapter
import com.example.radmushroom.weatherforecast.model.Forecast
import com.example.radmushroom.weatherforecast.util.Prefs
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {

    lateinit var forecastViewPagerAdapter: ForecastViewPagerAdapter
    var userEnteredString:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = Prefs(this)
        val prefsLocation:String = prefs.getLocation()
        getWeather(prefsLocation)

        locationNameId.setOnKeyListener { view, keycode, keyEvent ->
            if ((keyEvent.action == KeyEvent.ACTION_DOWN)
                    && (keycode == KeyEvent.KEYCODE_ENTER)) {
                Toast.makeText(this, locationNameId.text.toString(), Toast.LENGTH_SHORT).show()
                userEnteredString = locationNameId.text.toString().trim()
                prefs.setLocation(userEnteredString)
                getWeather(userEnteredString)
            }
            false
        }
    }

    private fun getImageUrl(html: String): String? {

        val imgRegex = "(?i)<img[^>]+?src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>"
//        val htmlString: String = "<![CDATA[<img src=\"http://l.yimg.com/a/i/us/we/52/27.gif\"/> <BR /> <b>Current Conditions:</b> <BR />Mostly Cloudy <BR /> <BR /> <b>Forecast:</b> <BR /> Sat - Rain. High: 78Low: 74 <BR /> Sun - Mostly Cloudy. High: 78Low: 77 <BR /> Mon - Partly Cloudy. High: 78Low: 76 <BR /> Tue - Sunny. High: 78Low: 74 <BR /> Wed - Sunny. High: 80Low: 72 <BR /> <BR /> <a href=\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-1545228/\">Full Forecast at Yahoo! Weather</a> <BR /> <BR /> <BR /> ]]>";
        var imgSrc: String? = null

        val p = Pattern.compile(imgRegex)
        val m = p.matcher(html)

        while (m.find()) {
            imgSrc = m.group(1)

        }
        return imgSrc
    }

    fun getWeather(currentLocation: String) {
        forecastViewPagerAdapter = ForecastViewPagerAdapter(supportFragmentManager, getFragments(currentLocation))
        viewPager.adapter = forecastViewPagerAdapter
    }

    private fun getFragments(locationString: String): List<android.support.v4.app.Fragment>{
        val fragmentList: MutableList<ForecastFragment> = mutableListOf()
        ForecastData().getForecast(object: ForecastListAsyncResponse{
            override fun processFinished(forecastMutableList: MutableList<Forecast>) {
                fragmentList.clear()
                val html: String = forecastMutableList[0].descriptionHTML!!
                Picasso.with(applicationContext)
                        .load(getImageUrl(html))
                        .into(weatherIcon)
                locationTextViewId.text = "${forecastMutableList[0].city}\n${forecastMutableList[0].country}"
                currentTemp.text = "${forecastMutableList[0].currentTemperature}°C"
                val date: List<String> = forecastMutableList[0].date!!.split(" ")
                val splitDate: String = "Today, ${date[0]} ${date[1]} ${date[2]} ${date[3]}"
                currentDateId.text = splitDate

                (1 until forecastMutableList.size).mapTo(fragmentList) { ForecastFragment()
                        .newInstance(forecastMutableList[it]) }
                forecastViewPagerAdapter.notifyDataSetChanged()
            }
        }, locationString)
        return fragmentList
    }
}
