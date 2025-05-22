package org.example.tasks;

import org.example.model.ClimateRecord;
import org.example.model.ClimateAverages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class YearlyAverageCalculatorTask implements Callable<Map<Integer, ClimateAverages>> {
    private final Map<Integer, List<ClimateRecord>> dataByYear;
    
    public YearlyAverageCalculatorTask(Map<Integer, List<ClimateRecord>> dataByYear) {
        this.dataByYear = dataByYear;
    }
    
    @Override
    public Map<Integer, ClimateAverages> call() {
        Map<Integer, ClimateAverages> yearlyAverages = new HashMap<>();
        
        for (Map.Entry<Integer, List<ClimateRecord>> entry : dataByYear.entrySet()) {
            int year = entry.getKey();
            List<ClimateRecord> yearData = entry.getValue();
            
            double tempAvg = yearData.stream().mapToDouble(ClimateRecord::temp).average().orElse(0.0);
            double humidityAvg = yearData.stream().mapToDouble(ClimateRecord::humidity).average().orElse(0.0);
            double windSpeedAvg = yearData.stream().mapToDouble(ClimateRecord::windSpeed).average().orElse(0.0);
            double visibilityAvg = yearData.stream().mapToDouble(ClimateRecord::visibility).average().orElse(0.0);
            double pressureAvg = yearData.stream().mapToDouble(ClimateRecord::pressure).average().orElse(0.0);
            
            yearlyAverages.put(year, new ClimateAverages(tempAvg, humidityAvg, windSpeedAvg, visibilityAvg, pressureAvg));
        }
        
        return yearlyAverages;
    }
}