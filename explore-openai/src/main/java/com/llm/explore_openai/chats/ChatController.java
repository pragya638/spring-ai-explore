package com.llm.explore_openai.chats;

import com.llm.explore_openai.dto.UserInput;

import reactor.core.publisher.Flux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;


@RestController
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    private boolean isJavaSpringQuestion(String prompt){
        String p=prompt.toLowerCase();
        return p.contains("java") || p.contains("spring")|| p.contains("jvm")
            || p.contains("spring")
            || p.contains("oop")
            || p.contains("collection")
            || p.contains("thread")
            || p.contains("exception")
            || p.contains("hibernate")|| p.contains("jpa")
            || p.contains("microservice")|| p.contains("rest api")||p.contains("spring");
    }
   @GetMapping("/chat")
public String chatGet() {
    return chatClient.prompt()
            .user("Say hello from Spring AI")
            .call()
            .content();
}


    @PostMapping("/v2/chats")
    public String chat(@RequestBody UserInput userInput) {
        String prompt=userInput.prompt();
        log.info("userInput: {}", userInput);
if(!isJavaSpringQuestion(prompt)){
    return"I don't know, but I'm sure it's interesting!";
}
        var systemMessage="""
                you are a java expert assistant ,who can answer java based
                questions.
                """;
        String content = chatClient.prompt()
        .system(systemMessage)   
        .user(userInput.prompt()) 
        .call()
        .content();


        log.info("content: {}", content);
        return content;
    }
    @PostMapping("v1/chats/stream")
    public Flux<String> chatWithStream(@RequestBody UserInput userInput){//Flux represents a reactive stream of data
        return chatClient.prompt().user(userInput.prompt())
        .stream().content().doOnNext(token -> log.info("token: {}", token));
    }
    
}
