package com.llm.explore_openai;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.llm.explore_openai.service.TtsService;

import java.io.File;

@RestController
public class AiTtsController {

    private final LlmService llmService;
    private final TtsService ttsService;

    public AiTtsController(LlmService llmService, TtsService ttsService) {
        this.llmService = llmService;
        this.ttsService = ttsService;
    }

    @GetMapping(value = "/ai-tts", produces = "audio/wav")
    public ResponseEntity<byte[]> aiTts(
            @RequestParam String prompt) throws Exception {

        // 1️⃣ LLM generates text
        String aiText = llmService.generateText(prompt);

        // 2️⃣ Text → Speech (BYTES)
        byte[] audio = ttsService.generateSpeechBytes(aiText);

        // 3️⃣ Return audio stream
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/wav"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=audio.wav")
                .body(audio);
    }
}


//this controller combines LLM text generation and TTS functionality
//it exposes a REST endpoint that takes a prompt, generates text using the LlmService, converts that text to speech using the TtsService, and returns the audio file as a response