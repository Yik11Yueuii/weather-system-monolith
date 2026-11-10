package dev.yik.weather.weather;

/** Simplified Open-Meteo WMO code mapping, enough for a student example. */
public final class WeatherDescriptions {
    private WeatherDescriptions() {}

    public static String of(int code) {
        return switch (code) {
            case 0 -> "晴";
            case 1, 2 -> "少云 / 多云";
            case 3 -> "阴";
            case 45, 48 -> "雾";
            case 51, 53, 55, 56, 57 -> "毛毛雨";
            case 61, 63, 65, 66, 67 -> "雨";
            case 71, 73, 75, 77 -> "雪";
            case 80, 81, 82 -> "阵雨";
            case 85, 86 -> "阵雪";
            case 95, 96, 99 -> "雷暴";
            default -> "天气变化";
        };
    }
}
