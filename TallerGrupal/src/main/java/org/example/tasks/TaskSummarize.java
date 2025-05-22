package org.example.tasks;

import java.time.format.DateTimeParseException;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

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
            
            // Agrupar datos por año
            Map<Integer, List<ClimateRecord>> dataByYear = data.stream()
                .collect(Collectors.groupingBy(record -> record.dateTime().getYear()));
            
            // Determinar el número de hilos basado en la cantidad de años
            int numYears = dataByYear.size();
            int numThreads = Math.min(numYears, Runtime.getRuntime().availableProcessors());
            
            // Crear un pool de hilos
            ExecutorService executor = Executors.newFixedThreadPool(numThreads + 1); // +1 para la tarea de extremos
            
            // Dividir los años en grupos para los hilos
            List<Map<Integer, List<ClimateRecord>>> yearGroups = new ArrayList<>();
            
            if (numYears <= numThreads) {
                // Un hilo por año
                for (Map.Entry<Integer, List<ClimateRecord>> entry : dataByYear.entrySet()) {
                    Map<Integer, List<ClimateRecord>> yearGroup = new HashMap<>();
                    yearGroup.put(entry.getKey(), entry.getValue());
                    yearGroups.add(yearGroup);
                }
            } else {
                // Agrupar años para distribuir entre los hilos disponibles
                int yearsPerThread = (int) Math.ceil((double) numYears / numThreads);
                Map<Integer, List<ClimateRecord>> currentGroup = new HashMap<>();
                int count = 0;
                
                for (Map.Entry<Integer, List<ClimateRecord>> entry : dataByYear.entrySet()) {
                    currentGroup.put(entry.getKey(), entry.getValue());
                    count++;
                    
                    if (count >= yearsPerThread) {
                        yearGroups.add(currentGroup);
                        currentGroup = new HashMap<>();
                        count = 0;
                    }
                }
                
                // Agregar el último grupo si no está vacío
                if (!currentGroup.isEmpty()) {
                    yearGroups.add(currentGroup);
                }
            }
            
            // Enviar tareas al pool para calcular promedios anuales
            List<Future<Map<Integer, ClimateAverages>>> yearlyAveragesFutures = new ArrayList<>();
            for (Map<Integer, List<ClimateRecord>> yearGroup : yearGroups) {
                Future<Map<Integer, ClimateAverages>> future = executor.submit(new YearlyAverageCalculatorTask(yearGroup));
                yearlyAveragesFutures.add(future);
            }
            
            // Obtener resultados de promedios anuales
            Map<Integer, ClimateAverages> yearlyAverages = new HashMap<>();
            for (Future<Map<Integer, ClimateAverages>> future : yearlyAveragesFutures) {
                yearlyAverages.putAll(future.get());
            }
            
            // Calcular promedios generales
            ClimateAverages generalAverages = new ClimateAverages(
                data.stream().mapToDouble(ClimateRecord::temp).average().orElse(0.0),
                data.stream().mapToDouble(ClimateRecord::humidity).average().orElse(0.0),
                data.stream().mapToDouble(ClimateRecord::windSpeed).average().orElse(0.0),
                data.stream().mapToDouble(ClimateRecord::visibility).average().orElse(0.0),
                data.stream().mapToDouble(ClimateRecord::pressure).average().orElse(0.0)
            );
            
            // Enviar tarea para calcular valores extremos
            Future<ExtremeValueFinderTask.ExtremeValues> extremesFuture = executor.submit(new ExtremeValueFinderTask(data));
            
            // Obtener resultados de valores extremos
            ExtremeValueFinderTask.ExtremeValues extremes = extremesFuture.get();
            
            // Apagar el executor
            try {
                executor.shutdown();
                if (!executor.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            
            // Crear y devolver el resumen
            return new ClimateSummary(
                generalAverages,
                yearlyAverages,
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