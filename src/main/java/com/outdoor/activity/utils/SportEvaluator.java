package com.outdoor.activity.utils;

import com.outdoor.activity.models.ForecastInfo;
import com.outdoor.activity.models.SportConfig;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SportEvaluator {

    public List<String> getSuitableHours(ForecastInfo info, SportConfig config) {
        List<String> suitableHours = new ArrayList<>();

        for (int i = 0; i < info.getTime().size(); i++) {
            double temperature = info.getTemperature_2m().get(i);
            double windGusts = info.getWind_gusts_10m().get(i);
            int precipitation = info.getPrecipitation_probability().get(i);
            int cloudCover = info.getCloud_cover().get(i);
            boolean isDay = info.getIsDay().get(i) == 1;

            boolean conditionsMet =
                    temperature >= config.getMinTemperature() &&
                            temperature <= config.getMaxTemperature() &&
                            windGusts <= config.getMaxWindSpeed() &&
                            precipitation < config.getMaxRainProbability() &&
                            cloudCover < config.getMaxCloudCover() &&
                            (!config.isRequiresDaylight() || isDay);

            if (conditionsMet) {
                suitableHours.add(info.getTime().get(i));
            }
        }

        return suitableHours;
    }
}