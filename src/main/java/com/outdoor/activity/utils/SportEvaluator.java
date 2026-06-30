package com.outdoor.activity.utils;

import com.outdoor.activity.models.DailyForecast;
import com.outdoor.activity.models.ForecastInfo;
import com.outdoor.activity.models.NotificationConfig;
import com.outdoor.activity.models.SportConfig;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//Може да се промени
@Component
public class SportEvaluator {
    private static final DateTimeFormatter WINDOW_TIME =
            DateTimeFormatter.ofPattern("HH:mm");

    public List<String> getSuitableHours(ForecastInfo info, SportConfig config) {
        List<String> suitableHours = new ArrayList<>();

       for (int i = 0; i < info.getTime().size(); i++) {
                double temperature = info.getTemperature_2m().get(i);
                double windGusts = info.getWind_gusts_10m().get(i);
                int precipitation = info.getPrecipitation_probability().get(i);
                int cloudCover = info.getCloud_cover().get(i);
                boolean isDay = info.getIsDay().get(i) == 1;

                boolean conditionsMet =
                        temperature >= config.getMinTemperature() &&
                                temperature <= config.getMaxTemperature() &&
                                windGusts <= config.getMaxWindSpeed() &&
                            precipitation < config.getMaxRainProbability() &&
                            cloudCover < config.getMaxCloudCover() &&
                            (!config.isRequiresDaylight() || isDay);

            if (conditionsMet) {
                suitableHours.add(info.getTime().get(i));
            }
        }

        return suitableHours;
    }

    public List<DailyForecast> filterByNotificationCriteria(List<DailyForecast> forecasts, NotificationConfig config) {
        LocalTime from = LocalTime.parse(config.getNotifyBetween().from(), WINDOW_TIME);
        LocalTime to = LocalTime.parse(config.getNotifyBetween().to(), WINDOW_TIME);

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

                if (overlapStart <= overlapEnd) {
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