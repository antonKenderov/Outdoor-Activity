package com.outdoor.activity.models;

import java.util.List;

public record ForecastInfo(
        List<Double> wind_gusts_10m,
        List<Integer> precipitation_probability,
        List<Integer> is_day,
        List<Double> temperature_2m,
        List<Integer> cloud_cover,
        List<String> time
) {}