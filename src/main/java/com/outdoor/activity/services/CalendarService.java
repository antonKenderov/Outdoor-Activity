package com.outdoor.activity.services;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.model.Events;
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
        if (eventExists(sport, start, end)) {
            return null;
        }

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

    private boolean eventExists(String sport, LocalDateTime start, LocalDateTime end) throws IOException {
        DateTime timeMin = new DateTime(
                start.atZone(ZoneId.of("Europe/Sofia")).toInstant().toEpochMilli());
        DateTime timeMax = new DateTime(
                end.atZone(ZoneId.of("Europe/Sofia")).toInstant().toEpochMilli());

        Events existingEvents = calendarService.events().list("primary")
                .setTimeMin(timeMin)
                .setTimeMax(timeMax)
                .setSingleEvents(true)
                .execute();

        for (Event existing : existingEvents.getItems()) {
            boolean sameSummary = sport.equals(existing.getSummary());
            boolean sameStart = existing.getStart() != null
                    && existing.getStart().getDateTime() != null
                    && existing.getStart().getDateTime().getValue() == timeMin.getValue();

            if (sameSummary && sameStart) {
                return true;
            }
        }
        return false;
    }
}
