package com.outdoor.activity.models;

import java.util.List;

public class DailyForecast {
    private String date;
    private List<String> hours;

    public DailyForecast() {}

    public DailyForecast(String date, List<String> hours) {
        this.date = date;
        this.hours = hours;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getHours() {
        return this.hours;
    }

    public void setHours(List<String> hours) {
        this.hours = hours;
    }
}
