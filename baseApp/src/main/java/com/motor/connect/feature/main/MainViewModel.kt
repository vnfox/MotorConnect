package com.motor.connect.feature.main

import android.location.Geocoder
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
import java.util.*


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
	
	fun getWeatherInfo(activity: MainActivity, latitude: Double, longitude: Double) {
		// get location
		var currentCity = "Thanh pho ho chi minh"
		if(latitude > 0 && longitude > 0) {
			val gcd = Geocoder(activity, Locale.getDefault())
			val addresses = gcd.getFromLocation(latitude, longitude, 1)
			currentCity = when {
				addresses.size > 0 -> {
					addresses[0].locality
				}
				else -> "Thanh pho ho chi minh"
			}
		}
		
		val weatherMap = WeatherMap(activity, MotorConstants.API_KEY_WEATHER)
		weatherMap.getCityWeather(currentCity, object : WeatherCallback() {
			override fun success(response: WeatherResponseModel) {
				
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
				//do nothing
			}
		})
	}
}