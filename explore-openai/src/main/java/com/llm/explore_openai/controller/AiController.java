package com.llm.explore_openai.controller;

import com.llm.explore_openai.service.MemoryHookService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final MemoryHookService memoryHookService;

    public AiController(MemoryHookService memoryHookService) {
        this.memoryHookService = memoryHookService;
    }

    @GetMapping("/memory-hook")
    public String memoryHook(@RequestParam String topic) {
        return memoryHookService.generateMemoryHook(topic);
    }
}
