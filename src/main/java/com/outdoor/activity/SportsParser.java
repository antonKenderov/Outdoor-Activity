package com.outdoor.activity;

import java.util.HashMap;

public class SportsParser {

    public void SportsParser() {
        HashMap<String, SportsFactory> registry = new HashMap<>();

        registry.put("soccer", new SoccerFactory());
    }
}
