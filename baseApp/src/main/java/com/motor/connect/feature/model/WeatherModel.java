package com.motor.connect.feature.model;

public class WeatherModel {

    private String cityName;
    private String temp;
    private String tempMin;
    private String tempMax;
    private String speed;
    private String urlWeather;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getUrlWeather() {
        return urlWeather;
    }

    public void setUrlWeather(String urlWeather) {
        this.urlWeather = urlWeather;
    }
}