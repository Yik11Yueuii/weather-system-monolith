package dev.yik.weather.weather;

import java.time.Instant;
import java.util.List;

/** source is 'demo' or 'open-meteo'; demo values must not be presented as real forecasts. */
public record WeatherReport(String cityId, String cityName, String source,
                            Instant observedAt, double temperature,
                            int humidity, int weatherCode, String description,
                            List<ForecastDay> forecast) {}
