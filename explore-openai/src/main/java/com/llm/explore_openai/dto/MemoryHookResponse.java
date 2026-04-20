package com.llm.explore_openai.dto;

public class MemoryHookResponse {

    private String explanation;
    private String hook;
    private String audioBase64;

    public MemoryHookResponse(String explanation, String hook, String audioBase64) {
        this.explanation = explanation;
        this.hook = hook;
        this.audioBase64 = audioBase64;
    }

    public String getExplanation() { return explanation; }
    public String getHook() { return hook; }
    public String getAudioBase64() { return audioBase64; }
}