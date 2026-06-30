package com.outdoor.activity.services;

import com.outdoor.activity.models.*;
import com.outdoor.activity.utils.ForecastFormatter;
import com.outdoor.activity.utils.SportEvaluator;
import com.outdoor.activity.utils.SportsParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WeatherService {

    private static final String URL = "https://api.open-meteo.com/v1/forecast" +
            "?latitude={latitude}" +
            "&longitude={longitude}" +
            "&hourly=wind_gusts_10m,precipitation_probability,is_day,temperature_2m,cloud_cover" +
            "&timezone=Europe/Sofia" +
            "&wind_speed_unit=ms" +
            "&forecast_days=16";

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private SportsParser sportsParser;

    @Autowired
    private SportEvaluator sportEvaluator;

    public ForecastInfo getForecast(double latitude, double longitude) {
        WeatherResponse response = restTemplate.getForObject(
                URL,
                WeatherResponse.class,
                latitude,
                longitude
        );
        return response.getHourly();
    }

    public List<DailyForecast> getSuitableForecast(String sport, double latitude, double longitude) {
        SportConfig config = sportsParser.getConfig(sport);
        if (config == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown sport: " + sport);
        }
        ForecastInfo forecast = getForecast(latitude, longitude);
        List<String> suitableHours = sportEvaluator.getSuitableHours(forecast, config);
        return ForecastFormatter.format(suitableHours, config.getMinDurationHours());
    }
}
