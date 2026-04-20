package com.llm.explore_openai.controller;

import com.llm.explore_openai.dto.MemoryHookResponse;
import com.llm.explore_openai.service.MemoryHookService;
import com.llm.explore_openai.service.TtsService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai-teacher")
public class AiTeacherController {

    private final MemoryHookService memoryHookService;
    private final TtsService ttsService;

    public AiTeacherController(MemoryHookService memoryHookService,
                               TtsService ttsService) {
        this.memoryHookService = memoryHookService;
        this.ttsService = ttsService;
    }

    @GetMapping(value = "/speak", produces = "audio/wav")
public byte[] speak(@RequestParam String topic) throws Exception {

    MemoryHookResponse response = memoryHookService.generateMemoryHook(topic);

    String fullText = response.getExplanation() + ". " + response.getHook();

    return ttsService.generateSpeechBytes(fullText);
}
}

// This controller provides an endpoint for the AI Teacher feature. It generates a memory hook based on the provided topic and then converts the explanation and hook into speech using the TTS service, returning the audio as a byte array.