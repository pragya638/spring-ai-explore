package com.llm.explore_openai.service;

import com.llm.explore_openai.dto.MemoryHookResponse;
import com.llm.explore_openai.prompts.MemoryHookPrompt;
import com.llm.explore_openai.rag.RagService;
import org.springframework.stereotype.Service;
import java.util.Base64;
@Service
public class MemoryHookService {

    private final AiService aiService;
    private final TtsService ttsService;
    private final RagService ragService;
private final ChatMemoryService chatMemoryService;

public MemoryHookService(AiService aiService,
                         TtsService ttsService,
                         RagService ragService,
                         ChatMemoryService chatMemoryService) {
    this.aiService = aiService;
    this.ttsService = ttsService;
    this.ragService = ragService;
    this.chatMemoryService = chatMemoryService;
}

    public MemoryHookResponse generateMemoryHook(String topic) {

        
        String context = ragService.getContext();
        if (!context.toLowerCase().contains(topic.toLowerCase())) {
    context = "No relevant context.";
}
String history = chatMemoryService.getRecentHistory(2); 
       
       String systemPrompt = """
Answer ONLY about this topic: %s

Use context only if relevant.

Context:
%s

Conversation:
%s

%s
""".formatted(topic, context, history, MemoryHookPrompt.system());
        
        String userPrompt = MemoryHookPrompt.user(topic);

        String response = aiService.askLLM(systemPrompt, userPrompt);

// ✅ Strong parsing
String explanation = "";
String hook = "";

String[] lines = response.split("\\n");

for (String line : lines) {
    if (line.toLowerCase().startsWith("explanation")) {
        explanation = line.replaceFirst("(?i)explanation:\\s*", "").trim();
    } 
    else if (line.toLowerCase().startsWith("memory hook")) {
        hook = line.replaceFirst("(?i)memory hook:\\s*", "").trim();
    }
}

// ✅ fallback (important)
if (explanation.isEmpty() || hook.isEmpty()) {
    explanation = response;
    hook = "Quick trick: remember the core idea!";
}
chatMemoryService.addMessage("User: " + topic);
chatMemoryService.addMessage("AI: " + explanation + " | " + hook);
        
        
String fullText = explanation + ". " + hook;




  return new MemoryHookResponse(explanation, hook, null);      
}
}