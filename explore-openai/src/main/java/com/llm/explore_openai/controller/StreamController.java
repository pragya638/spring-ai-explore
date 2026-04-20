package com.llm.explore_openai.controller;

import com.llm.explore_openai.service.AiService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class StreamController {

    private final AiService aiService;

    public StreamController(AiService aiService) {
        this.aiService = aiService;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String topic) {

        String systemPrompt = """
You are a helpful teacher.

Explain simply in 2 lines max.
""";

        String userPrompt = "Topic: " + topic;

        return aiService.streamLLM(systemPrompt, userPrompt);
    }
}