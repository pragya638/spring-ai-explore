package com.llm.explore_openai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

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
}
