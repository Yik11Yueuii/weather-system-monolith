package dev.yik.weather.city;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String cityId) {
        super("不支持的城市编号：" + cityId);
    }
}
