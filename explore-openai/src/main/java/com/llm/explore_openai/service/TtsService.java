package com.llm.explore_openai.service;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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

        process.getOutputStream().write(text.getBytes(StandardCharsets.UTF_8));
        process.getOutputStream().close();

        int exit = process.waitFor();
        if (exit != 0 || !tempFile.exists() || tempFile.length() == 0) {
            throw new RuntimeException("Piper TTS failed");
        }

        byte[] audio = Files.readAllBytes(tempFile.toPath());
        tempFile.delete(); // cleanup

        return audio;
    }
}
