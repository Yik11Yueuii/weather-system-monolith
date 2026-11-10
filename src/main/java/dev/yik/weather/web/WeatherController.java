package dev.yik.weather.web;

import dev.yik.weather.weather.WeatherReport;
import dev.yik.weather.weather.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService service;

    public WeatherController(WeatherService service) {
        this.service = service;
    }

    @GetMapping("/{cityId}")
    public WeatherReport byCity(@PathVariable String cityId) {
        return service.getWeather(cityId);
    }
}
