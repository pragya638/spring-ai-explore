package com.llm.explore_openai.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {

    private final RagService ragService;
    private final ChatClient chatClient;

    public RagController(RagService ragService, ChatClient chatClient) {
        this.ragService = ragService;
        this.chatClient = chatClient;
    }

    @GetMapping("/rag")
public String rag(@RequestParam String question) {
    return ragService.ask(question);
}
@GetMapping("/interview")
public String interview(@RequestParam String question) {
    return ragService.ask(question);
}


}
