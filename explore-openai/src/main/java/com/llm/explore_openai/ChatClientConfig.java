package com.llm.explore_openai;

import com.llm.explore_openai.tools.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(
            ChatClient.Builder builder,
            WeatherTool weatherTool
    ) {
        return builder
               
                .build();
    }
}

