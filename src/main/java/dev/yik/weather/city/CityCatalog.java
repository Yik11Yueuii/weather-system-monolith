package dev.yik.weather.city;

import java.util.List;
import org.springframework.stereotype.Service;

/** Small read-only list: later lessons can replace this with a MySQL repository. */
@Service
public class CityCatalog {
    private static final List<City> CITIES = List.of(
        new City("101010100", "北京", "北京", 39.9042, 116.4074),
        new City("101020100", "上海", "上海", 31.2304, 121.4737),
        new City("101280101", "广州", "广东", 23.1291, 113.2644),
        new City("101280601", "深圳", "广东", 22.5431, 114.0579),
        new City("101281601", "东莞", "广东", 23.0207, 113.7518),
        new City("101210101", "杭州", "浙江", 30.2741, 120.1551),
        new City("101270101", "成都", "四川", 30.5728, 104.0668),
        new City("101200101", "武汉", "湖北", 30.5928, 114.3055)
    );

    public List<City> findAll() {
        return CITIES;
    }

    public City getById(String cityId) {
        return CITIES.stream()
            .filter(city -> city.id().equals(cityId))
            .findFirst()
            .orElseThrow(() -> new CityNotFoundException(cityId));
    }
}
