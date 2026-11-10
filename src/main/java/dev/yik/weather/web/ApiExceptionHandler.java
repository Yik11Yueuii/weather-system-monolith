package dev.yik.weather.web;

import dev.yik.weather.city.CityNotFoundException;
import dev.yik.weather.weather.WeatherUnavailableException;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<ApiError> unknownCity(CityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ApiError("CITY_NOT_FOUND", ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(WeatherUnavailableException.class)
    public ResponseEntity<ApiError> unavailable(WeatherUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
            .body(new ApiError("WEATHER_UNAVAILABLE", ex.getMessage(), Instant.now()));
    }

    public record ApiError(String code, String message, Instant timestamp) {}
}
