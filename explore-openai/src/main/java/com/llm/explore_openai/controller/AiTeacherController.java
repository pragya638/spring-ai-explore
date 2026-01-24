package com.llm.explore_openai.controller;

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
        String hook = memoryHookService.generateMemoryHook(topic);
        return ttsService.generateSpeechBytes(hook);
    }
}

//this controller combines memory hook generation and TTS functionality
//it exposes a REST endpoint that takes a topic, generates a memory hook using the MemoryHookService, converts that text to speech using the TtsService, and returns the audio bytes as a response