package dev.yik.weather.weather;

import dev.yik.weather.city.City;

/** An interface: weather data can come from a fake provider or a real provider. */
public interface WeatherDataSource {
    WeatherReport load(City city);
}
