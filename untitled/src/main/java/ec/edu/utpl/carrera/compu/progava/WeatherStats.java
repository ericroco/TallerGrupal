package ec.edu.utpl.carrera.compu.progava;

import ec.edu.utpl.carrera.compu.progava.models.WeatherEntry;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.*;

public class WeatherStats {
    public static void main(String[] args) throws Exception {
        Reader reader = new FileReader("weatherHistory.csv");
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withTrim());

        List<WeatherEntry> entries = new ArrayList<>();
        for (CSVRecord record : csvParser) {
            try {
                entries.add(WeatherEntry.fromCsvRecord(record));
            } catch (Exception e) {
                System.err.println("Error al parsear línea: " + record.getRecordNumber());
            }
        }

        ExecutorService executor = Executors.newFixedThreadPool(6);
        List<Future<Map<String, Double>>> futures = new ArrayList<>();

        String[] metrics = {
                "temperature", "apparentTemperature", "humidity", "windSpeed", "visibility", "pressure"
        };

        for (String metric : metrics) {
            futures.add(executor.submit(new WeatherTask(entries, metric)));
        }

        System.out.println("Promedios anuales:");
        for (Future<Map<String, Double>> future : futures) {
            Map<String, Double> result = future.get();
            result.forEach((k, v) -> System.out.printf("%s: %.2f%n", k, v));
        }

        Comparator<WeatherEntry> byTemp = Comparator.comparingDouble(WeatherEntry::temperature);
        Comparator<WeatherEntry> byHumidity = Comparator.comparingDouble(WeatherEntry::humidity);
        Comparator<WeatherEntry> byVisibility = Comparator.comparingDouble(WeatherEntry::visibility);
        Comparator<WeatherEntry> byWind = Comparator.comparingDouble(WeatherEntry::windSpeed);

        WeatherEntry minTemp = Collections.min(entries, byTemp);
        WeatherEntry maxTemp = Collections.max(entries, byTemp);
        WeatherEntry minHumidity = Collections.min(entries, byHumidity);
        WeatherEntry maxHumidity = Collections.max(entries, byHumidity);
        WeatherEntry minVisibility = Collections.min(entries, byVisibility);
        WeatherEntry maxVisibility = Collections.max(entries, byVisibility);
        WeatherEntry maxWind = Collections.max(entries, byWind);

        System.out.println("\nExtremos:");
        System.out.printf("Más frío: %.2f°C en %s%n", minTemp.temperature(), minTemp.date());
        System.out.printf("Más calor: %.2f°C en %s%n", maxTemp.temperature(), maxTemp.date());
        System.out.printf("Menor humedad: %.2f en %s%n", minHumidity.humidity(), minHumidity.date());
        System.out.printf("Mayor humedad: %.2f en %s%n", maxHumidity.humidity(), maxHumidity.date());
        System.out.printf("Menor visibilidad: %.2f km en %s%n", minVisibility.visibility(), minVisibility.date());
        System.out.printf("Mayor visibilidad: %.2f km en %s%n", maxVisibility.visibility(), maxVisibility.date());
        System.out.printf("Mayor velocidad de viento: %.2f km/h en %s%n", maxWind.windSpeed(), maxWind.date());

        executor.shutdown();
    }
}
