package com.outdoor.activity.models;

public class SportConfig {
    private String name;
    private double minTemperature;
    private double maxTemperature;
    private double maxWindSpeed;
    private int maxRainProbability;
    private int maxCloudCover;
    private boolean requiresDaylight;
    private int minDurationHours;

    public SportConfig() {}

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