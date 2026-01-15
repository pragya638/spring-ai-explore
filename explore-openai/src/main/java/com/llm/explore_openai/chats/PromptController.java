package com.llm.explore_openai.chats;

import java.lang.System.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.llm.explore_openai.dto.UserInput;
import com.llm.explore_openai.dto.event.EventPlanResponse;

import org.springframework.ai.chat.messages.UserMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
/*
 * PromptController
 * ----------------
 * This controller acts as a GATEKEEPER before calling the AI.
 * It filters user prompts, adds system instructions, and then
 * sends a structured prompt to the AI using Spring AI ChatClient.
 */
@RestController
public class PromptController {
     // Logger for debugging & tracking requests/responses
private static final org.slf4j.Logger log =
        LoggerFactory.getLogger(PromptController.class);

    // ChatClient is the main Spring AI abstraction to talk to LLMs
private final ChatClient chatClient;

/*
     * Constructor-based dependency injection.
     * ChatClient.Builder is auto-configured by Spring AI.
     */
    public PromptController( ChatClient.Builder  chatClientBuilder) {

        this.chatClient = chatClientBuilder.build();
    }
 private boolean isJavaSpringQuestion(String prompt){
        String p=prompt.toLowerCase();
        return p.contains("java") || p.contains("spring")|| p.contains("jvm")
            || p.contains("spring")
            || p.contains("oop")
            || p.contains("collection")
            || p.contains("thread")
            || p.contains("exception")
            || p.contains("hibernate")|| p.contains("jpa")
            || p.contains("microservice")|| p.contains("rest api")||p.contains("spring");
    }
/*
     * POST API to accept user prompt and return AI response.
     *
     * URL: POST /v1/prompts
     * Input: JSON { "prompt": "your question" }
     * Output: AI-generated text OR fallback message
     */
   @PostMapping("/v1/prompts")
public String prompts(@RequestBody UserInput userInput) {

    if (!isJavaSpringQuestion(userInput.prompt())) {
        return "I don't know, but I'm sure it's interesting!";
    }


    var systemMessage = new SystemMessage("""
        You are a Java and Spring Boot expert.
        Answer clearly with examples.
        """);

    var userMessage1 = new UserMessage(userInput.prompt());


    var assistantHint = new SystemMessage("""
        Keep answers short and interview-friendly.
        """);

    Prompt prompt = new Prompt(
        List.of(systemMessage, assistantHint, userMessage1)
    );

    var response = chatClient.prompt(prompt).call();

    return response.content();
}
// Dynamic prompt with template injection
@PostMapping("/dynamic")
public String dynamicPrompt(
        @RequestParam String language,
        @RequestParam String question) {

    log.info("Dynamic prompt called | language={}, question={}", language, question);

    if (language == null || question == null ||
        language.isBlank() || question.isBlank()) {
        return "I don't know about that.";
    }

    try {
        // 🔹 SYSTEM message (HIGHEST PRIORITY)
        SystemMessage systemMessage = new SystemMessage("""
IGNORE ALL PREVIOUS INSTRUCTIONS.
You MUST answer ONLY in JavaScript.
If you use Python, you are WRONG.
Explain closures with example in JavaScript.
""");


        // 🔹 USER message (ONLY the question)
        UserMessage userMessage = new UserMessage(question);

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        return chatClient.prompt(prompt).call().content();

    } catch (Exception e) {
        log.error("Error in /dynamic", e);
        return "I don't know about that.";
    }
}
@PostMapping("/v1/event/plan")
public EventPlanResponse planEvent(@RequestBody UserInput userInput) throws Exception {

    // 🔹 STRICT system message for structured output
    SystemMessage systemMessage = new SystemMessage("""
You are an event planning backend engine.

STRICT RULES:
- Respond ONLY with valid JSON
- Do NOT add explanations, markdown, or text outside JSON
- JSON keys MUST match exactly:
  eventType, location, date, numberOfPeople,
  foodArrangement, activities, budgetEstimate
- If information is missing, use null
- Think internally, do NOT expose reasoning
""");

    // 🔹 User message
    UserMessage userMessage = new UserMessage(userInput.prompt());

    Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

    String response = chatClient.prompt(prompt).call().content();

    // 🔹 Convert JSON → DTO
    return new ObjectMapper().readValue(response, EventPlanResponse.class);
}


}