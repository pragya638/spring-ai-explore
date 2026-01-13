package com.llm.explore_openai.advisors;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
@Component
public class TeacherAdvisors {
    public  void before(String userQuestion){
        if(userQuestion==null|| userQuestion.isBlank()){
            throw new IllegalArgumentException(
                "❌ Message cannot be empty"
                );
        }
        String prompt=userQuestion.toLowerCase();
        // 🚨 Prompt Injection / Manipulation checks
        if (prompt.contains("ignore previous")
                || prompt.contains("ignore all instructions")
                || prompt.contains("change your role")
                || prompt.contains("act as")
                || prompt.contains("you are no longer")
                || prompt.contains("system prompt")
                || prompt.contains("developer message")
                || prompt.contains("jailbreak")) {

            throw new IllegalArgumentException(
                "This request is not allowed. Please ask an academic question."
            );
        }

        // 🚫 Non-educational content
        if (prompt.contains("hack")
                || prompt.contains("abuse")
                || prompt.contains("illegal")) {
            throw new IllegalArgumentException(
                "❌ Only academic-related questions are allowed");
            }
            System.out.println("TeacherAdvisor :Request Approved");
        }

    
}
