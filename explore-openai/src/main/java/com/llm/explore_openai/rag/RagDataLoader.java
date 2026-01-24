package com.llm.explore_openai.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class RagDataLoader {

    private final VectorStore vectorStore;

    public RagDataLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void load() throws Exception {
        var resource = new ClassPathResource("rag-docs/resume.txt");

        String content = new String(
                resource.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8
        );

        vectorStore.add(List.of(new Document(content)));
    }
}
//this runs once at startup to load documents into the vector store
//from a text file located in src/main/resources/rag-docs/notes.txt
//it reads the file content and adds it as a single document to the vector store
//this enables the RAG system to retrieve relevant information from these documents when answering questions
//knolwedge ingestion