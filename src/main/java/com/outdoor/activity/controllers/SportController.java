package com.outdoor.activity.controllers;

import com.outdoor.activity.sports.Badminton;
import com.outdoor.activity.models.DailyForecast;
import com.outdoor.activity.models.ForecastInfo;
import com.outdoor.activity.services.WeatherService;
import com.outdoor.activity.utils.ForecastFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sports")
public class SportController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/badminton")
    public List<DailyForecast> getBadmintonHours(
            @RequestParam double latitude,
            @RequestParam double longitude) {

        ForecastInfo forecast = weatherService.getForecast(latitude, longitude);
        Badminton badminton = new Badminton();
        List<String> suitableHours = badminton.getSuitableHours(forecast);
        return ForecastFormatter.format(suitableHours);
    }
}
