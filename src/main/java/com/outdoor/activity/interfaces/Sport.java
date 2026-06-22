package com.outdoor.activity.interfaces;

import com.outdoor.activity.models.ForecastInfo;

import java.util.List;

public interface Sport {
    List<String> getSuitableHours(ForecastInfo info);
}
