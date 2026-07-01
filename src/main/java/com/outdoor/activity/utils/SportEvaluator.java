package com.outdoor.activity.utils;

import com.outdoor.activity.models.DailyForecast;
import com.outdoor.activity.models.ForecastInfo;
import com.outdoor.activity.models.HourlyWeather;
import com.outdoor.activity.models.NotificationConfig;
import com.outdoor.activity.models.SportConfig;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class SportEvaluator {
    private static final DateTimeFormatter WINDOW_TIME =
            DateTimeFormatter.ofPattern("HH:mm");

    private final SportsParser sportsParser;

    public SportEvaluator(SportsParser sportsParser) {
        this.sportsParser = sportsParser;
    }

    public List<String> getSuitableHours(ForecastInfo info, SportConfig config) {
        List<Predicate<HourlyWeather>> criteria = SportCriteria.forConfig(config);
        List<String> suitableHours = new ArrayList<>();

        for (int i = 0; i < info.time().size(); i++) {
            HourlyWeather hour = new HourlyWeather(
                    info.time().get(i),
                    info.temperature_2m().get(i),
                    info.wind_gusts_10m().get(i),
                    info.precipitation_probability().get(i),
                    info.cloud_cover().get(i),
                    info.is_day().get(i) == 1
            );

            boolean conditionsMet = criteria.stream().allMatch(criterion -> criterion.test(hour));
            if (conditionsMet) {
                suitableHours.add(hour.time());
            }
        }

        return suitableHours;
    }

    public List<DailyForecast> filterByNotificationCriteria(List<DailyForecast> forecasts, NotificationConfig config) {
        LocalTime from = LocalTime.parse(config.getNotifyBetween().from(), WINDOW_TIME);
        LocalTime to = LocalTime.parse(config.getNotifyBetween().to(), WINDOW_TIME);
        int minDurationHours = sportsParser.getConfig(config.getSport()).getMinDurationHours();

        List<DailyForecast> filteredForecasts = new ArrayList<>();

        for (DailyForecast forecast : forecasts) {

            LocalDate date = LocalDate.parse(forecast.getDate());
            if (config.isWeekendOnly() && !(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                continue;
            }

            int fromHour = from.getHour();
            int toHour = to.getHour();

            List<String> matchingHours = new ArrayList<>();

            for (String range : forecast.getHours()) {
                String[] parts = range.split("-");
                int rangeStart = Integer.parseInt(parts[0]);
                int rangeEnd = Integer.parseInt(parts[1]);

                int overlapStart = Math.max(rangeStart, fromHour);
                int overlapEnd = Math.min(rangeEnd, toHour);

                if (overlapStart <= overlapEnd && (overlapEnd - overlapStart + 1) >= minDurationHours) {
                    matchingHours.add(String.format("%02d-%02d", overlapStart, overlapEnd));
                }
            }

            if (!matchingHours.isEmpty()) {
                DailyForecast newForecast = new DailyForecast(forecast.getDate(), matchingHours);
                filteredForecasts.add(newForecast);
            }
        }

        return filteredForecasts;
    }
}