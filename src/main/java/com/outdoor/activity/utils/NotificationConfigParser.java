package com.outdoor.activity.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outdoor.activity.models.NotificationConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class NotificationConfigParser {
    private List<NotificationConfig> notificationConfigs;

    @PostConstruct
    public void load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getResourceAsStream("/notification-config.json");
        this.notificationConfigs = mapper.readValue(is, mapper.getTypeFactory()
                .constructCollectionType(List.class, NotificationConfig.class));
    }

    public List<NotificationConfig> getAllConfigs() {
        return notificationConfigs;
    }
}
