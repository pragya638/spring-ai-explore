package com.llm.explore_openai.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class WeatherTool {

    private static final String URL =
        "https://api.openweathermap.org/data/2.5/weather?q=%s&units=metric&appid=%s";

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getWeather(String city) {
        String apiUrl = String.format(URL, city, apiKey);
        return restTemplate.getForObject(apiUrl, Map.class);
    }
}
