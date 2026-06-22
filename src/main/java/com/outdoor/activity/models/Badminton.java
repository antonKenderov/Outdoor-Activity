package com.outdoor.activity.models;

import com.outdoor.activity.interfaces.Sport;

import java.util.ArrayList;
import java.util.List;

public class Badminton implements Sport {

    @Override
    public List<String> getSuitableHours(ForecastInfo info) {
        List<String> suitableHours = new ArrayList<>();

        for (int i = 0; i < info.getTime().size(); i++) {
            double wind_gusts = info.getWind_gusts_10m().get(i);
            int precipitation_probability = info.getPrecipitation_probability().get(i);
            boolean isDay = info.getIsDay().get(i) == 1;
            double temperature_2m = info.getTemperature_2m().get(i);
            int cloud_cover = info.getCloud_cover().get(i);

            if (wind_gusts < 5
                    && precipitation_probability < 50
                    && isDay
                    && temperature_2m >= 10 && temperature_2m <= 30
                    && cloud_cover < 50) {
                suitableHours.add(info.getTime().get(i));
            }
        }

        return suitableHours;
    }
}
