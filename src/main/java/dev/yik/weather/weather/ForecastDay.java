package dev.yik.weather.weather;

import java.time.LocalDate;

public record ForecastDay(LocalDate date, int weatherCode, String description,
                          double minTemperature, double maxTemperature) {}
