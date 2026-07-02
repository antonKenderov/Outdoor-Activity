package com.outdoor.activity.models;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class SportConfig {
    @NotBlank
    private String name;

    @DecimalMin("-90.0")
    @DecimalMax("60.0")
    private double minTemperature;

    @DecimalMin("-90.0")
    @DecimalMax("60.0")
    private double maxTemperature;

    @PositiveOrZero
    private double maxWindSpeed;

    @Min(0)
    @Max(100)
    private int maxRainProbability;

    @Min(0)
    @Max(100)
    private int maxCloudCover;

    private boolean requiresDaylight;

    @Min(1)
    private int minDurationHours;

    public SportConfig() {}

    @AssertTrue(message = "minTemperature must be less than or equal to maxTemperature")
    public boolean isTemperatureRangeValid() {
        return minTemperature <= maxTemperature;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getMinTemperature() { return minTemperature; }
    public void setMinTemperature(double minTemperature) { this.minTemperature = minTemperature; }

    public double getMaxTemperature() { return maxTemperature; }
    public void setMaxTemperature(double maxTemperature) { this.maxTemperature = maxTemperature; }

    public double getMaxWindSpeed() { return maxWindSpeed; }
    public void setMaxWindSpeed(double maxWindSpeed) { this.maxWindSpeed = maxWindSpeed; }

    public int getMaxRainProbability() { return maxRainProbability; }
    public void setMaxRainProbability(int maxRainProbability) { this.maxRainProbability = maxRainProbability; }

    public int getMaxCloudCover() { return maxCloudCover; }
    public void setMaxCloudCover(int maxCloudCover) { this.maxCloudCover = maxCloudCover; }

    public boolean isRequiresDaylight() { return requiresDaylight; }
    public void setRequiresDaylight(boolean requiresDaylight) { this.requiresDaylight = requiresDaylight; }

    public int getMinDurationHours() { return minDurationHours; }
    public void setMinDurationHours(int minDurationHours) { this.minDurationHours = minDurationHours; }
}