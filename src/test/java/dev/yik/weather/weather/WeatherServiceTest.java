package dev.yik.weather.weather;

import dev.yik.weather.city.CityCatalog;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class WeatherServiceTest {
    @Test
    void cachesRepeatedRequestForSameCity() {
        AtomicInteger calls = new AtomicInteger();
        Clock clock = Clock.fixed(Instant.parse("2026-11-09T08:00:00Z"), ZoneOffset.UTC);
        WeatherDataSource source = city -> {
            calls.incrementAndGet();
            return new DemoWeatherDataSource(clock).load(city);
        };
        WeatherService service = new WeatherService(new CityCatalog(), source, clock, 600);
        WeatherReport first = service.getWeather("101020100");
        WeatherReport second = service.getWeather("101020100");
        assertSame(first, second);
        assertEquals(1, calls.get());
    }
}
