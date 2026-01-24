package com.llm.explore_openai.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Service
public class RagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagService(ChatClient chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    public String ask(String question) {

        var documents = vectorStore.similaritySearch(question);

        String context = documents.stream()
                .map(doc -> doc.getContent())
                .reduce("", (a, b) -> a + "\n" + b);

        return chatClient.prompt()
                .system("""
                    You are an interview bot.
Ask and answer questions ONLY from the candidate's resume.
If information is not found in resume, say "I don't know".
Be professional and concise.
                    CONTEXT:
                    """ + context)
                .user(question)
                .call()
                .content();
    }
}
//this service handles RAG operations by integrating the ChatClient and VectorStore
//the ask method retrieves relevant documents from the vector store based on the question
//and constructs a prompt that includes these documents as context for the chat model
//the chat model is then called to generate an answer based solely on the provided context