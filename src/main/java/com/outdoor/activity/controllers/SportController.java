package com.outdoor.activity.controllers;

import com.outdoor.activity.models.DailyForecast;
import com.outdoor.activity.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sports")
public class SportController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{sport}")
    public List<DailyForecast> getSuitableHours(
            @PathVariable String sport,
            @RequestParam double latitude,
            @RequestParam double longitude) {

        return weatherService.getSuitableForecast(sport, latitude, longitude);
    }
}

