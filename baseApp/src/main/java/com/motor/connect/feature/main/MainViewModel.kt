package com.motor.connect.feature.main

import android.util.Log
import com.motor.connect.base.BaseModel
import com.motor.connect.base.BaseViewModel
import com.motor.connect.feature.model.AreaModel
import com.motor.connect.feature.model.WeatherModel
import com.motor.connect.utils.MotorConstants
import com.orhanobut.hawk.Hawk
import github.vatsal.easyweather.Helper.TempUnitConverter
import github.vatsal.easyweather.Helper.WeatherCallback
import github.vatsal.easyweather.WeatherMap
import github.vatsal.easyweather.retrofit.models.WeatherResponseModel

class MainViewModel(mView: MainAreaView?, mModel: BaseModel)
    : BaseViewModel<MainAreaView, BaseModel>(mView, mModel) {

    var dataArea: MutableList<AreaModel> = mutableListOf()

    override fun initViewModel() {
    }

    fun initData(isFirst: Boolean?) {
        if (isFirst!!) {
            mView?.showEmptyView()
        } else {
            dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
            mView?.updateUI(dataArea)
        }
    }

    fun updateData() {
        dataArea = Hawk.get(MotorConstants.KEY_PUT_AREA_LIST)
        mView?.updateUI(dataArea)
    }

    fun stopUpdates() {
        dataArea.clear()
        Hawk.put(MotorConstants.KEY_PUT_AREA_LIST, dataArea)
    }

    fun getWeatherInfo(activity: MainActivity) {

        val weatherMap = WeatherMap(activity, MotorConstants.API_KEY_WEATHER)
        weatherMap.getCityWeather("Thanh pho ho chi minh", object : WeatherCallback() {
            override fun success(response: WeatherResponseModel) {

                val temperature = TempUnitConverter.convertToCelsius(response.main.temp)
                Log.d("hqdat", "======  weather temp  $temperature")

                var weather = WeatherModel()
                weather.cityName = response.name
                weather.speed = response.wind.speed
                weather.temp = TempUnitConverter.convertToCelsius(response.main.temp).toString()
                weather.tempMax = TempUnitConverter.convertToCelsius(response.main.temp_max).toString()
                weather.tempMin = TempUnitConverter.convertToCelsius(response.main.temp_min).toString()
                weather.urlWeather = response.weather[0].iconLink

                Hawk.put(MotorConstants.KEY_WEATHER_INFO, weather)
                mView?.updateWeatherInfo(weather)
            }

            override fun failure(message: String) {

            }
        })
    }
}