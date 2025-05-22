package org.example.model;

import java.time.LocalDateTime;

public record ClimateSummary(
    // Promedios
    double tempAvg, 
    double humAvg, 
    double windSpAvg, 
    double visibilityAvg, 
    double pressureAvg,
    
    // Valores extremos con sus fechas
    LocalDateTime coldestDateTime,
    double coldestTemp,
    LocalDateTime hottestDateTime,
    double hottestTemp,
    
    LocalDateTime lowestVisibilityDateTime,
    double lowestVisibility,
    LocalDateTime highestVisibilityDateTime,
    double highestVisibility,
    
    LocalDateTime lowestHumidityDateTime,
    double lowestHumidity,
    LocalDateTime highestHumidityDateTime,
    double highestHumidity,
    
    LocalDateTime lowestWindSpeedDateTime,
    double lowestWindSpeed,
    LocalDateTime highestWindSpeedDateTime,
    double highestWindSpeed
) {
    @Override
    public String toString() {
        return "Resumen Climático:\n" +
               "Promedios Anuales:\n" +
               "- Temperatura: " + tempAvg + " °C\n" +
               "- Humedad: " + humAvg + " %\n" +
               "- Velocidad del viento: " + windSpAvg + " km/h\n" +
               "- Visibilidad: " + visibilityAvg + " km\n" +
               "- Presión: " + pressureAvg + " millibars\n\n" +
               "Valores Extremos:\n" +
               "- Temperatura más baja: " + coldestTemp + " °C el " + coldestDateTime + "\n" +
               "- Temperatura más alta: " + hottestTemp + " °C el " + hottestDateTime + "\n" +
               "- Visibilidad más baja: " + lowestVisibility + " km el " + lowestVisibilityDateTime + "\n" +
               "- Visibilidad más alta: " + highestVisibility + " km el " + highestVisibilityDateTime + "\n" +
               "- Humedad más baja: " + lowestHumidity + " % el " + lowestHumidityDateTime + "\n" +
               "- Humedad más alta: " + highestHumidity + " % el " + highestHumidityDateTime + "\n" +
               "- Velocidad del viento más baja: " + lowestWindSpeed + " km/h el " + lowestWindSpeedDateTime + "\n" +
               "- Velocidad del viento más alta: " + highestWindSpeed + " km/h el " + highestWindSpeedDateTime;
    }
}