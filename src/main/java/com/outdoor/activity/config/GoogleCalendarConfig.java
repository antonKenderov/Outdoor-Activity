package com.outdoor.activity.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Configuration
public class GoogleCalendarConfig {

    @Bean
    public Calendar googleCalendar() throws IOException, GeneralSecurityException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(getClass().getResourceAsStream("/credentials.json"))
                .createScoped(List.of(CalendarScopes.CALENDAR));


        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(credentials))
                .setApplicationName("OutdoorActivity")
                .build();
    }
}
