package dev.yik.weather.web;

import dev.yik.weather.city.City;
import dev.yik.weather.city.CityCatalog;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    private final CityCatalog catalog;

    public CityController(CityCatalog catalog) {
        this.catalog = catalog;
    }

    @GetMapping
    public List<City> list() {
        return catalog.findAll();
    }
}
