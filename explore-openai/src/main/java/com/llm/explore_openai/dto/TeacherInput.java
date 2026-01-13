package com.llm.explore_openai.dto;

public record TeacherInput(String message) {

    public TeacherInput {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }
    }
}