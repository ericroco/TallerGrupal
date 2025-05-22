package org.example.tasks;

import org.example.model.ClimateRecord;

import java.util.List;
import java.util.concurrent.Callable;

public class AverageCalculatorTask implements Callable<Double[]> {
    private final List<ClimateRecord> data;
    
    public AverageCalculatorTask(List<ClimateRecord> data) {
        this.data = data;
    }
    
    @Override
    public Double[] call() {
        double tempAvg = data.stream().mapToDouble(ClimateRecord::temp).average().orElse(0.0);
        double humidityAvg = data.stream().mapToDouble(ClimateRecord::humidity).average().orElse(0.0);
        double windSpeedAvg = data.stream().mapToDouble(ClimateRecord::windSpeed).average().orElse(0.0);
        double visibilityAvg = data.stream().mapToDouble(ClimateRecord::visibility).average().orElse(0.0);
        double pressureAvg = data.stream().mapToDouble(ClimateRecord::pressure).average().orElse(0.0);
        
        return new Double[] {tempAvg, humidityAvg, windSpeedAvg, visibilityAvg, pressureAvg};
    }
}