package dev.yik.weather.weather;

import com.fasterxml.jackson.databind.JsonNode;
import dev.yik.weather.city.City;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/** Real forecast from the public Open-Meteo API; no credentials are embedded. */
@Component
@ConditionalOnProperty(name = "weather.mode", havingValue = "live")
public class OpenMeteoWeatherDataSource implements WeatherDataSource {
    private final RestClient client;

    public OpenMeteoWeatherDataSource() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(4000);
        factory.setReadTimeout(6000);
        this.client = RestClient.builder()
            .baseUrl("https://api.open-meteo.com/v1/forecast")
            .requestFactory(factory)
            .build();
    }

    @Override
    public WeatherReport load(City city) {
        try {
            JsonNode root = client.get().uri(uriBuilder -> uriBuilder
                    .queryParam("latitude", city.latitude())
                    .queryParam("longitude", city.longitude())
                    .queryParam("current", "temperature_2m,relative_humidity_2m,weather_code")
                    .queryParam("daily", "weather_code,temperature_2m_max,temperature_2m_min")
                    .queryParam("forecast_days", 7)
                    .queryParam("timezone", "Asia/Shanghai")
                    .build())
                .retrieve().body(JsonNode.class);

            if (root == null) {
                throw new WeatherUnavailableException("第三方天气服务返回空结果");
            }
            JsonNode current = root.path("current");
            JsonNode daily = root.path("daily");
            JsonNode dates = daily.path("time");
            JsonNode codes = daily.path("weather_code");
            JsonNode lows = daily.path("temperature_2m_min");
            JsonNode highs = daily.path("temperature_2m_max");
            if (!current.path("time").isTextual()
                || !current.path("temperature_2m").isNumber()
                || !current.path("relative_humidity_2m").isNumber()
                || !current.path("weather_code").isInt()
                || !dates.isArray() || dates.isEmpty()
                || codes.size() != dates.size()
                || lows.size() != dates.size()
                || highs.size() != dates.size()) {
                throw new WeatherUnavailableException("第三方天气数据结构不符合预期");
            }
            List<ForecastDay> forecast = new ArrayList<>();
            for (int i = 0; i < dates.size(); i++) {
                if (!dates.get(i).isTextual() || !codes.get(i).isInt()
                    || !lows.get(i).isNumber() || !highs.get(i).isNumber()) {
                    throw new WeatherUnavailableException("第三方预报数据缺失");
                }
                int code = codes.get(i).asInt();
                forecast.add(new ForecastDay(LocalDate.parse(dates.get(i).asText()),
                    code, WeatherDescriptions.of(code),
                    lows.get(i).asDouble(), highs.get(i).asDouble()));
            }
            int currentCode = current.path("weather_code").asInt();
            Instant observed = LocalDateTime.parse(current.path("time").asText())
                .atZone(ZoneId.of("Asia/Shanghai")).toInstant();
            return new WeatherReport(city.id(), city.name(), "open-meteo",
                observed, current.path("temperature_2m").asDouble(),
                current.path("relative_humidity_2m").asInt(), currentCode,
                WeatherDescriptions.of(currentCode), List.copyOf(forecast));
        } catch (WeatherUnavailableException ex) {
            throw ex;
        } catch (RestClientException | java.time.DateTimeException ex) {
            throw new WeatherUnavailableException("实时天气暂时不可用，请稍后重试", ex);
        }
    }
}
