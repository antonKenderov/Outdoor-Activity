package com.outdoor.activity.models;

public class WeatherResponse {
    private ForecastInfo hourly;

    public WeatherResponse() {}

    public ForecastInfo getHourly() {
        return hourly;
    }

    public void setHourly(ForecastInfo hourly) {
        this.hourly = hourly;
    }
}