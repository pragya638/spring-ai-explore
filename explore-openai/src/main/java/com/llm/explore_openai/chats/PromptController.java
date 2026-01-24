package com.llm.explore_openai.chats;

import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.llm.explore_openai.dto.UserInput;
import com.llm.explore_openai.dto.event.EventPlanResponse;
import com.llm.explore_openai.tools.CurrentTimeTool;
import com.llm.explore_openai.tools.WeatherTool;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prompts")
public class PromptController {

    private static final org.slf4j.Logger log =
            LoggerFactory.getLogger(PromptController.class);

    private final ChatClient chatClient;

    private final CurrentTimeTool timeTool;

    private final WeatherTool weatherTool;

    public PromptController(ChatClient.Builder chatClientBuilder,CurrentTimeTool timeTool, WeatherTool weatherTool) {
        this.timeTool=timeTool;
        this.weatherTool=weatherTool;
        this.chatClient = chatClientBuilder.build();
    }

    /* -------------------------------------------------------
       1️⃣ BASIC PROMPT (NO STRUCTURE)
       ------------------------------------------------------- */
    @PostMapping("/v1/prompts")
    public String prompts(@RequestBody UserInput userInput) {

        SystemMessage systemMessage = new SystemMessage("""
            You are a Java and Spring Boot expert.
            Answer clearly with examples.
        """);

        Prompt prompt = new Prompt(
                List.of(systemMessage, new UserMessage(userInput.prompt()))
        );

        return chatClient.prompt(prompt).call().content();
    }

    /* -------------------------------------------------------
       2️⃣ MANUAL JSON → OBJECTMAPPER (LEARNING MODE)
       ------------------------------------------------------- */
    @PostMapping("/v1/event/plan")
    public EventPlanResponse planEvent(@RequestBody UserInput userInput) throws Exception {

        SystemMessage systemMessage = new SystemMessage("""
            You are an event planning backend engine.
            Respond ONLY with valid JSON.
            Do not add extra text.
        """);

        String response = chatClient
                .prompt()
                .call()
                .content();

        log.info("Raw LLM JSON: {}", response);

        return new ObjectMapper().readValue(response, EventPlanResponse.class);
    }

    /* -------------------------------------------------------
       3️⃣ ENTITY / FUNCTION-STYLE (BEST PRACTICE)
       ------------------------------------------------------- */
    @PostMapping("/v1/event/plan/entity")
    public EventPlanResponse planEventWithEntity(@RequestBody UserInput userInput) {

        BeanOutputParser<EventPlanResponse> parser =
                new BeanOutputParser<>(EventPlanResponse.class);

        SystemMessage systemMessage = new SystemMessage("""
            You are an event planning engine.
            Extract event details.
        """ + parser.getFormat());

        return chatClient
                .prompt()
                .call()
                .entity(EventPlanResponse.class);
    }

    /* -------------------------------------------------------
       4️⃣ PARAMETERIZED TYPE REFERENCE (LIGHT STRUCTURE)
       ------------------------------------------------------- */
    @PostMapping("/v1/event/plan/typed")
    public EventPlanResponse planEventTyped(@RequestBody UserInput userInput) {

        SystemMessage systemMessage = new SystemMessage("""
            Return ONLY valid JSON matching an event plan.
            Do not add explanations.
        """);

        return chatClient
                .prompt()
                .call()
                .entity(new ParameterizedTypeReference<EventPlanResponse>() {});
    }

    /* -------------------------------------------------------
       5️⃣ MAP OUTPUT (VERY FLEXIBLE, LOW SAFETY)
       ------------------------------------------------------- */
    @PostMapping("/v1/event/plan/map")
    public Map<String, Object> planEventAsMap(@RequestBody UserInput userInput) {

        SystemMessage systemMessage = new SystemMessage("""
            Return ONLY valid JSON.
        """);

        return chatClient
                .prompt()
                .call()
                .entity(new ParameterizedTypeReference<Map<String, Object>>() {});
    }
@GetMapping("/chat")
public String chat(@RequestParam String question) {

    String q = question.toLowerCase();

    if (q.contains("weather")) {
        String city = extractCity(q);
        return formatWeather(weatherTool.getWeather(city));
    }

    return chatClient.prompt()
            .user(question)
            .call()
            .content();
}

@SuppressWarnings("unchecked")
private String formatWeather(Map<String, Object> weather) {

    // 🔴 Safety checks
    if (weather == null || weather.containsKey("error")) {
        return "⚠️ Weather service unavailable for this location.";
    }

    Map<String, Object> main = (Map<String, Object>) weather.get("main");
    List<Map<String, Object>> weatherList =
            (List<Map<String, Object>>) weather.get("weather");

    if (main == null || weatherList == null || weatherList.isEmpty()) {
        return "⚠️ Weather data incomplete.";
    }

    Map<String, Object> weatherDetails = weatherList.get(0);

    String description = (String) weatherDetails.get("description");
    double temperature = ((Number) main.get("temp")).doubleValue();
    int humidity = ((Number) main.get("humidity")).intValue();
    String city = (String) weather.get("name");

    return String.format(
            "🌦 Current weather in %s: %s, Temperature: %.1f°C, Humidity: %d%%",
            city, description, temperature, humidity
    );
}


private String extractCity(String q) {

    q = q.toLowerCase();

    if (q.contains("delhi")) return "Delhi,IN";
    if (q.contains("london")) return "London,GB";
    if (q.contains("new york")) return "New York,US";
    if (q.contains("tokyo")) return "Tokyo,JP";
    if (q.contains("paris")) return "Paris,FR";
    if (q.contains("berlin")) return "Berlin,DE";
    if (q.contains("mumbai")) return "Mumbai,IN";
    if (q.contains("bangalore")) return "Bangalore,IN";

    // 🟡 fallback (VERY important)
    return null;
}

}