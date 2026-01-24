package com.llm.explore_openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class LlmService {

    private final ChatClient chatClient;

    public LlmService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String generateText(String userPrompt) {
        return chatClient
                .prompt(userPrompt)
                .call()
                .content();
    }
}//this service encapsulates the functionality to interact with a language model via the ChatClient
//the generateText method takes a user prompt as input and returns the generated text response from the model
