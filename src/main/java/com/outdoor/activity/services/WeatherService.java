package com.outdoor.activity.services;

import com.outdoor.activity.models.WeatherResponse;
import com.outdoor.activity.models.ForecastInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private static final String URL = "https://api.open-meteo.com/v1/forecast" +
            "?latitude={latitude}" +
            "&longitude={longitude}" +
            "&hourly=wind_gusts_10m,precipitation_probability,is_day,temperature_2m,cloud_cover" +
            "&timezone=Europe/Sofia" +
            "&forecast_days=3";

    private final RestTemplate restTemplate = new RestTemplate();

    public ForecastInfo getForecast(double latitude, double longitude) {
        WeatherResponse response = restTemplate.getForObject(
                URL,
                WeatherResponse.class,
                latitude,
                longitude
        );
        return response.getHourly();
    }
}