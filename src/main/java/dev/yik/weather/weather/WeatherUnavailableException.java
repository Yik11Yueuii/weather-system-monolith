package dev.yik.weather.weather;

public class WeatherUnavailableException extends RuntimeException {
    public WeatherUnavailableException(String message) {
        super(message);
    }
    public WeatherUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
