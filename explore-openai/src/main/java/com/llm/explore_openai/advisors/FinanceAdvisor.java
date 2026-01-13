package com.llm.explore_openai.advisors;

import org.springframework.stereotype.Component;

@Component
public class FinanceAdvisor {

    public void before(String userPrompt) {

        System.out.println("🔍 FinanceAdvisor BEFORE");

        if (userPrompt == null || userPrompt.isBlank()) {
            throw new IllegalArgumentException("❌ Message cannot be empty");
        }

        String prompt = userPrompt.toLowerCase();

        if (!(prompt.contains("finance")
                || prompt.contains("money")
                || prompt.contains("investment")
                || prompt.contains("stock"))) {

            throw new IllegalArgumentException(
                    "❌ Only finance-related questions are allowed"
            );
        }
    }

    public void after(String aiResponse) {
        System.out.println("✅ FinanceAdvisor AFTER");
        System.out.println("AI Response: " + aiResponse);
    }
}
