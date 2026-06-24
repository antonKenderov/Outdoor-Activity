package com.outdoor.activity.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outdoor.activity.models.SportConfig;
import com.outdoor.activity.models.SportsConfigWrapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SportsParser {

    private final Map<String, SportConfig> registry = new HashMap<>();

    @PostConstruct
    public void load() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getResourceAsStream("/sports-config.json");
        SportsConfigWrapper wrapper = mapper.readValue(is, SportsConfigWrapper.class);
        List<SportConfig> sports = wrapper.getSports();

        for (SportConfig sport : sports) {
            registry.put(sport.getName(), sport);
        }
    }

    public SportConfig getConfig(String sportName) {
        return registry.get(sportName);
    }
}