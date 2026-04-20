package com.llm.explore_openai.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

@Service
public class TtsService {

    private static final String MODEL_PATH =
            "C:\\piper\\models\\en_US-amy-medium.onnx";

    private static final String TEMP_DIR =
            System.getProperty("java.io.tmpdir");

    public byte[] generateSpeechBytes(String text) throws Exception {

        File tempFile = File.createTempFile("tts_", ".wav", new File(TEMP_DIR));

        ProcessBuilder pb = new ProcessBuilder(
                "py", "-3.10",
                "-m", "piper",
                "--model", MODEL_PATH,
                "--output_file", tempFile.getAbsolutePath()
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        // Send text
        try (OutputStream os = process.getOutputStream()) {
    os.write(text.getBytes(StandardCharsets.UTF_8));
    os.flush();   
}

        // Capture logs (IMPORTANT)
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("Piper: " + line);
        }

        boolean finished = process.waitFor(20, TimeUnit.SECONDS);

        if (!finished || process.exitValue() != 0 ||
                !tempFile.exists() || tempFile.length() == 0) {
            throw new RuntimeException("TTS generation failed");
        }

        byte[] audio = Files.readAllBytes(tempFile.toPath());

        tempFile.delete();

        return audio;
    }
}