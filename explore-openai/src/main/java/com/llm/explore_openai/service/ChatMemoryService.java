package com.llm.explore_openai.service;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
@Service
public class ChatMemoryService {

    private final List<String> messages = new ArrayList<>();

    public void addMessage(String message) {
        messages.add(message);
    }

    public String getHistory() {
        return String.join("\n", messages);
    }

    public String getRecentHistory(int n) {
        if (messages.isEmpty()) return "";

        int start = Math.max(messages.size() - n, 0);

        StringBuilder sb = new StringBuilder();

        for (int i = start; i < messages.size(); i++) {
            sb.append(messages.get(i)).append("\n");
        }

        return sb.toString();
    }

    public void clear() {
        messages.clear();
    }
}