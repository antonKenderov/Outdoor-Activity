package com.outdoor.activity.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outdoor.activity.models.SportConfig;
import com.outdoor.activity.models.SportsConfigWrapper;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SportsParser {

    private final Map<String, SportConfig> registry = new HashMap<>();
    private final Validator validator;

    public SportsParser(Validator validator) {
        this.validator = validator;
    }

    @PostConstruct
    public void load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getResourceAsStream("/sports-config.json");
        SportsConfigWrapper wrapper = mapper.readValue(is, SportsConfigWrapper.class);

        Set<ConstraintViolation<SportsConfigWrapper>> violations = validator.validate(wrapper);
        if (!violations.isEmpty()) {
            throw new IllegalStateException("Invalid sports-config.json: " + describe(violations));
        }

        List<SportConfig> sports = wrapper.getSports();

        for (SportConfig sport : sports) {
            registry.put(sport.getName(), sport);
        }
    }

    private String describe(Set<? extends ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .collect(Collectors.joining("; "));
    }

    public SportConfig getConfig(String sportName) {
        return registry.get(sportName);
    }
}