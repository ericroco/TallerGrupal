package org.example;

import org.example.tasks.*;
import org.example.model.ClimateSummary;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) {
        try {
            // Crear un pool de hilos
            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            
            // Crear y enviar la tarea
            TaskSummarize task = new TaskSummarize("TallerGrupal/weatherHistory.csv");
            Future<ClimateSummary> future = executor.submit(task);
            
            // Esperar y obtener el resultado
            ClimateSummary result = future.get(10, TimeUnit.SECONDS);
            System.out.println(result);
            
            // Apagar el executor
            executor.shutdown();
            
        } catch (Exception e) {
            System.out.println("Error al procesar los datos climáticos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}