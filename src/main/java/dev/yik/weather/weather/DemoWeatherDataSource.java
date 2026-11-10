package dev.yik.weather.weather;

import dev.yik.weather.city.City;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/** Repeatable fake forecast for learning without network, database, or API keys. */
@Component
@ConditionalOnProperty(name = "weather.mode", havingValue = "demo", matchIfMissing = true)
public class DemoWeatherDataSource implements WeatherDataSource {
    private final Clock clock;

    public DemoWeatherDataSource(Clock clock) {
        this.clock = clock;
    }

    @Override
    public WeatherReport load(City city) {
        LocalDate today = LocalDate.now(clock.withZone(ZoneId.of("Asia/Shanghai")));
        int[] codes = {2, 0, 3, 61, 1, 80, 2};
        int shift = Math.floorMod(city.id().hashCode(), 7);
        List<ForecastDay> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int code = codes[(i + shift) % codes.length];
            days.add(new ForecastDay(today.plusDays(i), code,
                WeatherDescriptions.of(code), 17 + i % 3 + shift % 3,
                24 + i % 4 + shift % 3));
        }
        int currentCode = days.get(0).weatherCode();
        return new WeatherReport(city.id(), city.name(), "demo", clock.instant(),
            22 + shift, 60 + shift * 2, currentCode,
            WeatherDescriptions.of(currentCode), List.copyOf(days));
    }
}
