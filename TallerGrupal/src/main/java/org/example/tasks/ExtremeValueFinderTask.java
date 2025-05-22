package org.example.tasks;

import org.example.model.ClimateRecord;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

public class ExtremeValueFinderTask implements Callable<ExtremeValueFinderTask.ExtremeValues> {
    private final List<ClimateRecord> data;
    
    public ExtremeValueFinderTask(List<ClimateRecord> data) {
        this.data = data;
    }
    
    @Override
    public ExtremeValues call() {
        // Temperatura
        ClimateRecord coldest = data.stream()
                .min(Comparator.comparingDouble(ClimateRecord::temp))
                .orElse(null);
                
        ClimateRecord hottest = data.stream()
                .max(Comparator.comparingDouble(ClimateRecord::temp))
                .orElse(null);
        
        // Visibilidad
        ClimateRecord lowestVisibility = data.stream()
                .min(Comparator.comparingDouble(ClimateRecord::visibility))
                .orElse(null);
                
        ClimateRecord highestVisibility = data.stream()
                .max(Comparator.comparingDouble(ClimateRecord::visibility))
                .orElse(null);
        
        // Humedad
        ClimateRecord lowestHumidity = data.stream()
                .min(Comparator.comparingDouble(ClimateRecord::humidity))
                .orElse(null);
                
        ClimateRecord highestHumidity = data.stream()
                .max(Comparator.comparingDouble(ClimateRecord::humidity))
                .orElse(null);
        
        // Velocidad del viento
        ClimateRecord lowestWindSpeed = data.stream()
                .min(Comparator.comparingDouble(ClimateRecord::windSpeed))
                .orElse(null);
                
        ClimateRecord highestWindSpeed = data.stream()
                .max(Comparator.comparingDouble(ClimateRecord::windSpeed))
                .orElse(null);
        
        return new ExtremeValues(
            coldest != null ? coldest.dateTime() : null,
            coldest != null ? coldest.temp() : 0.0,
            hottest != null ? hottest.dateTime() : null,
            hottest != null ? hottest.temp() : 0.0,
            
            lowestVisibility != null ? lowestVisibility.dateTime() : null,
            lowestVisibility != null ? lowestVisibility.visibility() : 0.0,
            highestVisibility != null ? highestVisibility.dateTime() : null,
            highestVisibility != null ? highestVisibility.visibility() : 0.0,
            
            lowestHumidity != null ? lowestHumidity.dateTime() : null,
            lowestHumidity != null ? lowestHumidity.humidity() : 0.0,
            highestHumidity != null ? highestHumidity.dateTime() : null,
            highestHumidity != null ? highestHumidity.humidity() : 0.0,
            
            lowestWindSpeed != null ? lowestWindSpeed.dateTime() : null,
            lowestWindSpeed != null ? lowestWindSpeed.windSpeed() : 0.0,
            highestWindSpeed != null ? highestWindSpeed.dateTime() : null,
            highestWindSpeed != null ? highestWindSpeed.windSpeed() : 0.0
        );
    }
    
    public static class ExtremeValues {
        // Temperatura
        public final LocalDateTime coldestDateTime;
        public final double coldestTemp;
        public final LocalDateTime hottestDateTime;
        public final double hottestTemp;
        
        // Visibilidad
        public final LocalDateTime lowestVisibilityDateTime;
        public final double lowestVisibility;
        public final LocalDateTime highestVisibilityDateTime;
        public final double highestVisibility;
        
        // Humedad
        public final LocalDateTime lowestHumidityDateTime;
        public final double lowestHumidity;
        public final LocalDateTime highestHumidityDateTime;
        public final double highestHumidity;
        
        // Velocidad del viento
        public final LocalDateTime lowestWindSpeedDateTime;
        public final double lowestWindSpeed;
        public final LocalDateTime highestWindSpeedDateTime;
        public final double highestWindSpeed;
        
        public ExtremeValues(
            LocalDateTime coldestDateTime, double coldestTemp,
            LocalDateTime hottestDateTime, double hottestTemp,
            LocalDateTime lowestVisibilityDateTime, double lowestVisibility,
            LocalDateTime highestVisibilityDateTime, double highestVisibility,
            LocalDateTime lowestHumidityDateTime, double lowestHumidity,
            LocalDateTime highestHumidityDateTime, double highestHumidity,
            LocalDateTime lowestWindSpeedDateTime, double lowestWindSpeed,
            LocalDateTime highestWindSpeedDateTime, double highestWindSpeed
        ) {
            this.coldestDateTime = coldestDateTime;
            this.coldestTemp = coldestTemp;
            this.hottestDateTime = hottestDateTime;
            this.hottestTemp = hottestTemp;
            
            this.lowestVisibilityDateTime = lowestVisibilityDateTime;
            this.lowestVisibility = lowestVisibility;
            this.highestVisibilityDateTime = highestVisibilityDateTime;
            this.highestVisibility = highestVisibility;
            
            this.lowestHumidityDateTime = lowestHumidityDateTime;
            this.lowestHumidity = lowestHumidity;
            this.highestHumidityDateTime = highestHumidityDateTime;
            this.highestHumidity = highestHumidity;
            
            this.lowestWindSpeedDateTime = lowestWindSpeedDateTime;
            this.lowestWindSpeed = lowestWindSpeed;
            this.highestWindSpeedDateTime = highestWindSpeedDateTime;
            this.highestWindSpeed = highestWindSpeed;
        }
    }
}