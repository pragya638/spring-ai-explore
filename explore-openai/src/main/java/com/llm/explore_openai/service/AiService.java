package com.llm.explore_openai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
 import reactor.core.publisher.Flux;
@Service
public class AiService {
    private final ChatClient chatClient;
    public AiService(ChatClient chatClient){
        this.chatClient=chatClient;
    }
    
    public String askLLM(String systemPrompt,String userPrompt){
        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
    }
   

public Flux<String> streamLLM(String systemPrompt, String userPrompt) {

    return chatClient.prompt()
            .system(systemPrompt)
            .user(userPrompt)
            .stream()
            .content();
}
}
