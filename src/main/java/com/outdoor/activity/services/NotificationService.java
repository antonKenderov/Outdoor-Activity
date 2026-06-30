package com.outdoor.activity.services;

import com.outdoor.activity.models.DailyForecast;
import com.outdoor.activity.models.NotificationConfig;
import com.outdoor.activity.utils.NotificationConfigParser;
import com.outdoor.activity.utils.SportEvaluator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationConfigParser notificationConfigParser;
    private final WeatherService weatherService;
    private final SportEvaluator sportEvaluator;
    private final EmailService emailService;

    @Value("${notification.latitude}")
    private double latitude;

    @Value("${notification.longitude}")
    private double longitude;

    public NotificationService(NotificationConfigParser notificationConfigParser, WeatherService weatherService, SportEvaluator sportEvaluator, EmailService emailService) {
        this.notificationConfigParser = notificationConfigParser;
        this.weatherService = weatherService;
        this.sportEvaluator = sportEvaluator;
        this.emailService = emailService;
    }

    @Scheduled(fixedRateString = "${notification.check-interval-ms}")
    public void checkAndNotify() {
        List<NotificationConfig> configs = notificationConfigParser.getAllConfigs();

        for (NotificationConfig config : configs) {

            try {
                List<DailyForecast> forecasts = weatherService.getSuitableForecast(
                        config.getSport(), latitude, longitude);

                List<DailyForecast> matching = sportEvaluator.filterByNotificationCriteria(
                        forecasts, config);

                if (!matching.isEmpty()) {
                    String subject = "Предпочитани дати";
                    String body = "Подходящи дати: " + config.getSport() + "\n";
                    for (DailyForecast forecast : matching) {
                        body += forecast.getDate() + forecast.getHours() + "\n";
                    };

                    emailService.sendEmail(config.getEmail(), subject, body);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }





}
