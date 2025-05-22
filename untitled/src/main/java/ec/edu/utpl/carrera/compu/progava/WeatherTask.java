package ec.edu.utpl.carrera.compu.progava;

import ec.edu.utpl.carrera.compu.progava.models.WeatherEntry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class WeatherTask implements Callable<Map<String, Double>> {
    private final List<WeatherEntry> entries;
    private final String metric;

    public WeatherTask(List<WeatherEntry> entries, String metric) {
        this.entries = entries;
        this.metric = metric;
    }

    @Override
    public Map<String, Double> call() {
        double sum = 0.0;

        for (WeatherEntry entry : entries) {
            switch (metric) {
                case "temperature" -> sum += entry.temperature();
                case "apparentTemperature" -> sum += entry.apparentTemperature();
                case "humidity" -> sum += entry.humidity();
                case "windSpeed" -> sum += entry.windSpeed();
                case "visibility" -> sum += entry.visibility();
                case "pressure" -> sum += entry.pressure();
            }
        }

        double average = sum / entries.size();
        return Map.of(metric, average);
    }
}
