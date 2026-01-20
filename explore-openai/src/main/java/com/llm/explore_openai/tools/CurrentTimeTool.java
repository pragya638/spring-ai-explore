package com.llm.explore_openai.tools;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class CurrentTimeTool {

    public String getCurrentTime() {
        return LocalDateTime.now().toString();
    }
}