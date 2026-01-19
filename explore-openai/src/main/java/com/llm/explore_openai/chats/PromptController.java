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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prompts")
public class PromptController {

    private static final org.slf4j.Logger log =
            LoggerFactory.getLogger(PromptController.class);

    private final ChatClient chatClient;

    private final CurrentTimeTool timeTool;

    public PromptController(ChatClient.Builder chatClientBuilder,CurrentTimeTool timeTool) {
        this.timeTool=timeTool;
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

    // ✅ STEP 1: NORMALIZE INPUT (THIS IS THE KEY)
    String q = question
            .toLowerCase()
            .replaceAll("[^a-z ]", "")   // remove ?, . ! etc
            .replaceAll("\\s+", " ")     // normalize spaces
            .trim();

    // DEBUG (optional but helpful)
    System.out.println("NORMALIZED QUESTION = [" + q + "]");

    // ✅ STEP 2: REGION MATCHING (JAVA ONLY)
    if (q.contains("india")) {
        return timeForZone("Asia/Kolkata", "India");
    }

    if (q.contains("uk") || q.contains("london")) {
        return timeForZone("Europe/London", "UK");
    }

    if (q.contains("new york") || q.contains("usa") || q.contains("america")) {
        return timeForZone("America/New_York", "USA (New York)");
    }

    if (q.contains("california") || q.contains("los angeles")) {
        return timeForZone("America/Los_Angeles", "USA (California)");
    }

    if (q.contains("japan")) {
        return timeForZone("Asia/Tokyo", "Japan");
    }

    // 🤖 STEP 3: ONLY IF NO MATCH → LLM
    return chatClient.prompt()
            .user(question)
            .call()
            .content();
}
private String timeForZone(String zoneId, String label) {
    return "Current time in " + label + " is: " +
            java.time.ZonedDateTime.now(java.time.ZoneId.of(zoneId));
}
}