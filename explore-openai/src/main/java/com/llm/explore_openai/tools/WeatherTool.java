package com.llm.explore_openai.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class WeatherTool {

    private static final Logger log = LoggerFactory.getLogger(WeatherTool.class);

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getWeather(String city) {

        String apiUrl = String.format("%s?q=%s&units=metric&appid=%s",
                baseUrl, city, apiKey);

        log.info("Calling Weather API: {}", apiUrl);

        try {
            return restTemplate.getForObject(apiUrl, Map.class);
        } catch (Exception e) {
            log.error("Weather API failed for city={}", city, e);
            return Map.of(
                    "error", true,
                    "city", city,
                    "message", e.getMessage()
            );
        }
    }
}
