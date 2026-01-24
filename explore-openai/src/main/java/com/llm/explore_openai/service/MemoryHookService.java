package com.llm.explore_openai.service;

import org.springframework.stereotype.Service;

@Service
public class MemoryHookService {

    private final AiService aiService;

    public MemoryHookService(AiService aiService) {
        this.aiService = aiService;
    }

    public String generateMemoryHook(String topic) {

    String systemPrompt = """
You are an expert computer science teacher.

Your task:
- First, explain the concept in ONE very simple line (student-friendly).
- Then, give ONE short catchy memory hook or rhyme.
- Total output should be maximum 2–3 lines.
- Use simple English, no jargon.
- Avoid long explanations.
- The student should both UNDERSTAND and REMEMBER the concept.
""";


    String userPrompt = """
    Topic: %s
    Create ONE powerful memory hook.
    """.formatted(topic);

    return aiService.askLLM(systemPrompt, userPrompt);
}

}
