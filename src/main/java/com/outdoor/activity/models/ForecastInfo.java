package com.outdoor.activity.models;

import java.util.List;

public class ForecastInfo {
    private List<Double> wind_gusts_10m;
    private List<Integer> precipitation_probability;
    private List<Integer> is_day;
    private List<Double> temperature_2m;
    private List<Integer> cloud_cover;
    private List<String> time;

    public ForecastInfo() {}

    public ForecastInfo(List<Double> wind_gusts_10m, List<Integer> precipitation_probability, List<Integer> is_day, List<Double> temperature_2m, List<Integer> cloud_cover, List<String> time) {
        this.wind_gusts_10m = wind_gusts_10m;
        this.precipitation_probability = precipitation_probability;
        this.is_day = is_day;
        this.temperature_2m = temperature_2m;
        this.cloud_cover = cloud_cover;
        this.time = time;
    }

    public List<Double> getWind_gusts_10m() {
        return this.wind_gusts_10m;
    }

    public List<Integer> getPrecipitation_probability() {
        return this.precipitation_probability;
    }

    public List<Integer> getIsDay() {
        return this.is_day;
    }

    public List<Double> getTemperature_2m() {
        return this.temperature_2m;
    }

    public List<Integer> getCloud_cover() {
        return this.cloud_cover;
    }

    public List<String> getTime() {
        return this.time;
    }

}
