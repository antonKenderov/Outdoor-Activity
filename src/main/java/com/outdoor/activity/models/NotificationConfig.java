package com.outdoor.activity.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class NotificationConfig {
    @NotBlank
    private String sport;

    private boolean weekendOnly;

    @NotBlank
    @Email
    private String email;

    @NotNull
    @Valid
    private TimeWindow notifyBetween;

    public record TimeWindow(
            @NotBlank @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "must be in HH:mm format") String from,
            @NotBlank @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "must be in HH:mm format") String to
    ) {
        @AssertTrue(message = "from must be before to")
        public boolean isRangeValid() {
            return from == null || to == null || from.compareTo(to) < 0;
        }
    }

    public String getSport() { return sport; }
    public void setSport(String sport) { this.sport = sport; }

    public boolean isWeekendOnly() { return weekendOnly; }
    public void setWeekendOnly(boolean weekendOnly) { this.weekendOnly = weekendOnly; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public TimeWindow getNotifyBetween() { return notifyBetween; }
    public void setNotifyBetween(TimeWindow notifyBetween) { this.notifyBetween = notifyBetween; }
}
