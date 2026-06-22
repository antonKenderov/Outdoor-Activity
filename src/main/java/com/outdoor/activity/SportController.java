package com.outdoor.activity;

import com.outdoor.activity.models.Badminton;
import com.outdoor.activity.models.ForecastInfo;
import com.outdoor.activity.services.WeatherService;
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
    public List<String> getBadmintonHours(
            @RequestParam double latitude,
            @RequestParam double longitude) {

        ForecastInfo forecast = weatherService.getForecast(latitude, longitude);
        Badminton badminton = new Badminton();
        return badminton.getSuitableHours(forecast);
    }
}
