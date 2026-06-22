package com.outdoor.activity.utils;

import com.outdoor.activity.models.DailyForecast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ForecastFormatter {

    private static final DateTimeFormatter ISO_HOUR =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private ForecastFormatter() {
    }

    public static List<DailyForecast> format(List<String> hours) {
        if (hours == null || hours.isEmpty()) {
            return new ArrayList<>();
        }

        Map<LocalDate, List<Integer>> hoursByDate = new LinkedHashMap<>();
        for (String value : hours) {
            LocalDateTime dateTime = LocalDateTime.parse(value, ISO_HOUR);
            hoursByDate
                    .computeIfAbsent(dateTime.toLocalDate(), d -> new ArrayList<>())
                    .add(dateTime.getHour());
        }

        List<DailyForecast> result = new ArrayList<>();
        for (Map.Entry<LocalDate, List<Integer>> entry : hoursByDate.entrySet()) {
            String date = entry.getKey().toString();
            List<String> ranges = toRanges(entry.getValue());
            result.add(new DailyForecast(date, ranges));
        }
        return result;
    }

    private static List<String> toRanges(List<Integer> hours) {
        List<Integer> sorted = hours.stream().distinct().sorted().toList();

        List<String> ranges = new ArrayList<>();
        int start = sorted.get(0);
        int previous = start;

        for (int i = 1; i < sorted.size(); i++) {
            int current = sorted.get(i);
            if (current == previous + 1) {
                previous = current;
            } else {
                ranges.add(formatRange(start, previous));
                start = current;
                previous = current;
            }
        }
        ranges.add(formatRange(start, previous));
        return ranges;
    }

    private static String formatRange(int start, int end) {
        return String.format("%02d-%02d", start, end);
    }
}
