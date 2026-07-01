package com.outdoor.activity.services;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class CalendarService {

    private final Calendar calendarService;

    public CalendarService(@Lazy Calendar calendarService) {
        this.calendarService = calendarService;
    }

    public Event createEvent(String sport, LocalDateTime start, LocalDateTime end) throws IOException {
        Event event = new Event()
                .setSummary(sport)
                .setDescription("Температура + прогноза");

        EventDateTime startTime = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(
                        start.atZone(ZoneId.of("Europe/Sofia")).toInstant().toEpochMilli()))
                .setTimeZone("Europe/Sofia");
        event.setStart(startTime);

        EventDateTime endTime = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(
                        end.atZone(ZoneId.of("Europe/Sofia")).toInstant().toEpochMilli()))
                .setTimeZone("Europe/Sofia");
        event.setEnd(endTime);

        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(List.of(
                        new EventReminder().setMethod("email").setMinutes(24 * 60),
                        new EventReminder().setMethod("popup").setMinutes(10)
                ));
        event.setReminders(reminders);

        return calendarService.events().insert("primary", event).execute();
    }
}
