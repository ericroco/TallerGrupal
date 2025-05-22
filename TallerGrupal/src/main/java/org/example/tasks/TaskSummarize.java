package org.example.tasks;

import org.example.model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskSummarize implements Callable<ClimateSummary> {
    private final String path2Data;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS Z");

    public TaskSummarize(String path2Data) {
        this.path2Data = path2Data;
    }

    @Override
    public ClimateSummary call() throws Exception {
        try {
            // Cargar los datos
            List<ClimateRecord> data = getDataAsList(path2Data);
            
            // Crear un pool de hilos
            ExecutorService executor = Executors.newFixedThreadPool(2);
            
            // Enviar tareas al pool
            Future<Double[]> averagesFuture = executor.submit(new AverageCalculatorTask(data));
            Future<ExtremeValueFinderTask.ExtremeValues> extremesFuture = executor.submit(new ExtremeValueFinderTask(data));
            
            // Obtener resultados
            Double[] averages = averagesFuture.get();
            ExtremeValueFinderTask.ExtremeValues extremes = extremesFuture.get();
            
            // Apagar el executor
            executor.shutdown();
            
            // Crear y devolver el resumen
            return new ClimateSummary(
                averages[0], averages[1], averages[2], averages[3], averages[4],
                extremes.coldestDateTime, extremes.coldestTemp,
                extremes.hottestDateTime, extremes.hottestTemp,
                extremes.lowestVisibilityDateTime, extremes.lowestVisibility,
                extremes.highestVisibilityDateTime, extremes.highestVisibility,
                extremes.lowestHumidityDateTime, extremes.lowestHumidity,
                extremes.highestHumidityDateTime, extremes.highestHumidity,
                extremes.lowestWindSpeedDateTime, extremes.lowestWindSpeed,
                extremes.highestWindSpeedDateTime, extremes.highestWindSpeed
            );
            
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Leer CSV usando Apache Commons CSV
    private List<ClimateRecord> getDataAsList(String path2Data) throws IOException {
        List<ClimateRecord> output = new ArrayList<>();
        var csvFormat = CSVFormat
            .RFC4180
            .builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .get();
        try(Reader reader = Files.newBufferedReader(Paths.get(path2Data));
            CSVParser parser = CSVParser.parse(reader, csvFormat)) {

            for(var csvRecord : parser) {
                LocalDateTime dateTime = LocalDateTime.parse(csvRecord.get("Formatted Date"), formatter);
                var temp = Double.parseDouble(csvRecord.get("Temperature (C)"));
                var humidity = Double.parseDouble(csvRecord.get("Humidity"));
                var windSpeed = Double.parseDouble(csvRecord.get("Wind Speed (km/h)"));
                var visibility = Double.parseDouble(csvRecord.get("Visibility (km)"));
                var pressure = Double.parseDouble(csvRecord.get("Pressure (millibars)"));

                output.add(new ClimateRecord(dateTime, temp, humidity, windSpeed, visibility, pressure));
            }
        }

        return output;
    }
}