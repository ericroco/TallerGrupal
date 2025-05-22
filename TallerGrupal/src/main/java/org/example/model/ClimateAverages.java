package org.example.model;

public record ClimateAverages(
    double temperature,
    double humidity,
    double windSpeed,
    double visibility,
    double pressure
) {}