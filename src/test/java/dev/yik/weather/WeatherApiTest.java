package dev.yik.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "weather.mode=demo")
@AutoConfigureMockMvc
class WeatherApiTest {
    @Autowired MockMvc mvc;

    @Test
    void listsCities() throws Exception {
        mvc.perform(get("/api/cities"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("北京"));
    }

    @Test
    void returnsDemoWeather() throws Exception {
        mvc.perform(get("/api/weather/101020100"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cityName").value("上海"))
            .andExpect(jsonPath("$.source").value("demo"))
            .andExpect(jsonPath("$.forecast.length()").value(7));
    }

    @Test
    void rejectsUnknownCity() throws Exception {
        mvc.perform(get("/api/weather/not-a-city"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.code").value("CITY_NOT_FOUND"));
    }
}
