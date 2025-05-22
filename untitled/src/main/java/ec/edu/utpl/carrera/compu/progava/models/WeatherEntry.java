package     ec.edu.utpl.carrera.compu.progava.models;

public record WeatherEntry(
        String date,
        String summary,
        String precipType,
        double temperature,
        double apparentTemperature,
        double humidity,
        double windSpeed,
        double visibility,
        double pressure
) {
    public static WeatherEntry fromCsvRecord(org.apache.commons.csv.CSVRecord record) {
        return new WeatherEntry(
                record.get("Formatted Date"),
                record.get("Summary"),
                record.get("Precip Type"),
                Double.parseDouble(record.get("Temperature (C)")),
                Double.parseDouble(record.get("Apparent Temperature (C)")),
                Double.parseDouble(record.get("Humidity")),
                Double.parseDouble(record.get("Wind Speed (km/h)")),
                Double.parseDouble(record.get("Visibility (km)")),
                Double.parseDouble(record.get("Pressure (millibars)"))
        );
    }
}

