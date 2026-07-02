package com.outdoor.activity.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outdoor.activity.models.NotificationConfig;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NotificationConfigParser {
    private final Validator validator;
    private List<NotificationConfig> notificationConfigs;

    public NotificationConfigParser(Validator validator) {
        this.validator = validator;
    }

    @PostConstruct
    public void load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getResourceAsStream("/notification-config.json");
        List<NotificationConfig> configs = mapper.readValue(is, mapper.getTypeFactory()
                .constructCollectionType(List.class, NotificationConfig.class));

        for (NotificationConfig config : configs) {
            Set<ConstraintViolation<NotificationConfig>> violations = validator.validate(config);
            if (!violations.isEmpty()) {
                String description = violations.stream()
                        .map(v -> v.getPropertyPath() + " " + v.getMessage())
                        .collect(Collectors.joining("; "));
                throw new IllegalStateException("Invalid notification-config.json entry for sport '"
                        + config.getSport() + "': " + description);
            }
        }

        this.notificationConfigs = configs;
    }

    public List<NotificationConfig> getAllConfigs() {
        return notificationConfigs;
    }
}
