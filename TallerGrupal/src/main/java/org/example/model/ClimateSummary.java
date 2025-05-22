package org.example.model;

import java.time.LocalDateTime;
import java.util.Map;

public record ClimateSummary(
    ClimateAverages generalAverages,
    Map<Integer, ClimateAverages> yearlyAverages,
    
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
        StringBuilder sb = new StringBuilder();
        sb.append("Resumen Climático:\n");
        
        // Promedios anuales
        sb.append("Promedios Anuales:\n");
        for (Map.Entry<Integer, ClimateAverages> entry : yearlyAverages.entrySet()) {
            int year = entry.getKey();
            ClimateAverages averages = entry.getValue();
            sb.append("Año ").append(year).append(":\n");
            sb.append("  - Temperatura: ").append(averages.temperature()).append(" °C\n");
            sb.append("  - Humedad: ").append(averages.humidity()).append(" %\n");
            sb.append("  - Velocidad del viento: ").append(averages.windSpeed()).append(" km/h\n");
            sb.append("  - Visibilidad: ").append(averages.visibility()).append(" km\n");
            sb.append("  - Presión: ").append(averages.pressure()).append(" millibars\n");
        }
        
        // Valores extremos
        sb.append("\nValores Extremos:\n");
        sb.append("- Temperatura más baja: ").append(coldestTemp).append(" °C el ").append(coldestDateTime).append("\n");
        sb.append("- Temperatura más alta: ").append(hottestTemp).append(" °C el ").append(hottestDateTime).append("\n");
        sb.append("- Visibilidad más baja: ").append(lowestVisibility).append(" km el ").append(lowestVisibilityDateTime).append("\n");
        sb.append("- Visibilidad más alta: ").append(highestVisibility).append(" km el ").append(highestVisibilityDateTime).append("\n");
        sb.append("- Humedad más baja: ").append(lowestHumidity).append(" % el ").append(lowestHumidityDateTime).append("\n");
        sb.append("- Humedad más alta: ").append(highestHumidity).append(" % el ").append(highestHumidityDateTime).append("\n");
        sb.append("- Velocidad del viento más baja: ").append(lowestWindSpeed).append(" km/h el ").append(lowestWindSpeedDateTime).append("\n");
        sb.append("- Velocidad del viento más alta: ").append(highestWindSpeed).append(" km/h el ").append(highestWindSpeedDateTime);
        
        return sb.toString();
    }
}