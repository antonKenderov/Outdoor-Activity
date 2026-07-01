package com.outdoor.activity.utils;

import com.outdoor.activity.models.HourlyWeather;
import com.outdoor.activity.models.SportConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class SportCriteria {

    private SportCriteria() {}

    public static List<Predicate<HourlyWeather>> forConfig(SportConfig config) {
        List<Predicate<HourlyWeather>> criteria = new ArrayList<>();

        criteria.add(hour -> hour.temperature() >= config.getMinTemperature());
        criteria.add(hour -> hour.temperature() <= config.getMaxTemperature());
        criteria.add(hour -> hour.windGusts() <= config.getMaxWindSpeed());
        criteria.add(hour -> hour.precipitationProbability() < config.getMaxRainProbability());
        criteria.add(hour -> hour.cloudCover() < config.getMaxCloudCover());

        if (config.isRequiresDaylight()) {
            criteria.add(HourlyWeather::isDay);
        }

        return criteria;
    }
}
