package org.example.tasks;

import org.example.model.ClimateRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;

public class ExtremeValueFinderTask implements Callable<ExtremeValueFinderTask.ExtremeValues> {
    private final List<ClimateRecord> data;

    public ExtremeValueFinderTask(List<ClimateRecord> data) {
        this.data = data;
    }

    @Override
    public ExtremeValues call() {
        if (data == null || data.isEmpty()) {
            return new ExtremeValues(null, 0.0, null, 0.0, null, 0.0, null, 0.0, null, 0.0, null, 0.0, null, 0.0, null, 0.0);
        }

        ClimateRecord coldest = data.get(0);
        ClimateRecord hottest = data.get(0);
        ClimateRecord lowestVisibility = data.get(0);
        ClimateRecord highestVisibility = data.get(0);
        ClimateRecord lowestHumidity = data.get(0);
        ClimateRecord highestHumidity = data.get(0);
        ClimateRecord lowestWindSpeed = data.get(0);
        ClimateRecord highestWindSpeed = data.get(0);

        for (ClimateRecord record : data) {
            if (record.temp() < coldest.temp()) coldest = record;
            if (record.temp() > hottest.temp()) hottest = record;

            if (record.visibility() < lowestVisibility.visibility()) lowestVisibility = record;
            if (record.visibility() > highestVisibility.visibility()) highestVisibility = record;

            if (record.humidity() < lowestHumidity.humidity()) lowestHumidity = record;
            if (record.humidity() > highestHumidity.humidity()) highestHumidity = record;

            if (record.windSpeed() < lowestWindSpeed.windSpeed()) lowestWindSpeed = record;
            if (record.windSpeed() > highestWindSpeed.windSpeed()) highestWindSpeed = record;
        }

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