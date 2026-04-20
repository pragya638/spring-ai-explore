package com.llm.explore_openai.controller;

import com.llm.explore_openai.dto.AiRequest;
import com.llm.explore_openai.dto.MemoryHookResponse;
import com.llm.explore_openai.service.ChatMemoryService;
import com.llm.explore_openai.service.MemoryHookService;
import org.springframework.web.bind.annotation.*;
import com.llm.explore_openai.dto.AiRequest;

@RestController
@RequestMapping("/ai")
public class AiController {

    private final MemoryHookService memoryHookService;
private final ChatMemoryService chatMemoryService;

public AiController(MemoryHookService memoryHookService,
                    ChatMemoryService chatMemoryService) {
    this.memoryHookService = memoryHookService;
    this.chatMemoryService = chatMemoryService;
}

    @GetMapping("/memory-hook")
    public MemoryHookResponse memoryHook(@RequestParam String topic) {
        return memoryHookService.generateMemoryHook(topic);
    }
    @PostMapping("/ask")
public MemoryHookResponse ask(@RequestBody AiRequest request) {
    return memoryHookService.generateMemoryHook(request.getTopic());
}
@PostMapping("/clear")
public String clearMemory() {
    chatMemoryService.clear();
    return "Memory cleared";
}
}