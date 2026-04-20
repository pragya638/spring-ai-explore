package com.llm.explore_openai.rag;

import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class RagService {

    public String getContext() {
        try {
            return Files.readString(Path.of("src/main/resources/rag-docs/notes.txt"));
        } catch (Exception e) {
            return "";
        }
    }

    public String ask(String question) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ask'");
    }
}