package com.outdoor.activity.services;

import com.outdoor.activity.models.DailyForecast;
import com.outdoor.activity.models.NotificationConfig;
import com.outdoor.activity.utils.EmailTemplateBuilder;
import com.outdoor.activity.utils.NotificationConfigParser;
import com.outdoor.activity.utils.SportEvaluator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationConfigParser notificationConfigParser;
    private final EmailTemplateBuilder htmlBuilder;
    private final WeatherService weatherService;
    private final SportEvaluator sportEvaluator;
    private final EmailService emailService;
    private final CalendarService calendarService;

    @Value("${notification.latitude}")
    private double latitude;

    @Value("${notification.longitude}")
    private double longitude;

    public NotificationService(NotificationConfigParser notificationConfigParser, EmailTemplateBuilder htmlBuilder, WeatherService weatherService, SportEvaluator sportEvaluator, EmailService emailService, CalendarService calendarService) {
        this.notificationConfigParser = notificationConfigParser;
        this.htmlBuilder = htmlBuilder;
        this.weatherService = weatherService;
        this.sportEvaluator = sportEvaluator;
        this.emailService = emailService;
        this.calendarService = calendarService;
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
                    String subject = "Идеално време за " + config.getSport();
                    String htmlBody = htmlBuilder.buildHtmlBody(config.getSport(), matching);

                    emailService.sendEmail(config.getEmail(), subject, htmlBody);
                    createEvent(config.getSport(), matching);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createEvent(String sport, List<DailyForecast> matching) throws java.io.IOException {
        for (DailyForecast forecast : matching) {
            LocalDate date = LocalDate.parse(forecast.getDate());

            for (String hourRange : forecast.getHours()) {
                String[] parts = hourRange.split("-");
                LocalDateTime start = date.atTime(Integer.parseInt(parts[0]), 0);
                LocalDateTime end = date.atTime(Integer.parseInt(parts[1]), 0);

                calendarService.createEvent(sport, start, end);
            }
        }
    }
}
