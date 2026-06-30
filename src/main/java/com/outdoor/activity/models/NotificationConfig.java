package com.outdoor.activity.models;

public class NotificationConfig {
    private String sport;
    private boolean weekendOnly;
    private String season;
    private String email;
    private TimeWindow notifyBetween;

    record TimeWindow(String from, String to) { }

    public String getSport() { return sport; }
    public void setSport(String sport) { this.sport = sport; }

    public boolean isWeekendOnly() { return weekendOnly; }
    public void setWeekendOnly(boolean weekendOnly) { this.weekendOnly = weekendOnly; }

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public TimeWindow getNotifyBetween() { return notifyBetween; }
    public void setNotifyBetween(TimeWindow notifyBetween) { this.notifyBetween = notifyBetween; }
}
