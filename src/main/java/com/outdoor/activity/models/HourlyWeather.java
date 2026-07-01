package com.outdoor.activity.models;

public record HourlyWeather(
        String time,
        double temperature,
        double windGusts,
        int precipitationProbability,
        int cloudCover,
        boolean isDay
) {
}
