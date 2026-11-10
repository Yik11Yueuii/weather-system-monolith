package dev.yik.weather.weather;

import dev.yik.weather.city.City;
import dev.yik.weather.city.CityCatalog;
import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** One-process memory cache. Later lessons can introduce Redis without changing the controller. */
@Service
public class WeatherService {
    private final CityCatalog cities;
    private final WeatherDataSource dataSource;
    private final Clock clock;
    private final long ttlSeconds;
    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public WeatherService(CityCatalog cities, WeatherDataSource dataSource,
                          Clock clock, @Value("${weather.cache-seconds:600}") long ttlSeconds) {
        this.cities = cities;
        this.dataSource = dataSource;
        this.clock = clock;
        this.ttlSeconds = Math.max(0, ttlSeconds);
    }

    public WeatherReport getWeather(String cityId) {
        City city = cities.getById(cityId); // Check city before using cache / external APIs.
        Instant now = clock.instant();
        CacheEntry previous = cache.get(city.id());
        if (previous != null && now.isBefore(previous.expiresAt())) {
            return previous.report();
        }
        WeatherReport fresh = dataSource.load(city);
        if (ttlSeconds > 0) {
            cache.put(city.id(), new CacheEntry(fresh, now.plusSeconds(ttlSeconds)));
        }
        return fresh;
    }

    private record CacheEntry(WeatherReport report, Instant expiresAt) {}
}
