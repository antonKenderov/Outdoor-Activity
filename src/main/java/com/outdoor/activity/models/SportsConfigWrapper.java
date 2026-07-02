package com.outdoor.activity.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class SportsConfigWrapper {
    @NotEmpty
    private List<@Valid SportConfig> sports;

    public List<SportConfig> getSports() { return sports; }
    public void setSports(List<SportConfig> sports) { this.sports = sports; }
}
