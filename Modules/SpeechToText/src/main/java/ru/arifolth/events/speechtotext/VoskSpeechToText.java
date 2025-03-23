package ru.arifolth.events.speechtotext;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.vosk.Model;
import org.vosk.Recognizer;
import ru.arifolth.events.components.ISpeechToText;

import java.io.IOException;
import java.io.InputStream;

@Component
public class VoskSpeechToText implements ISpeechToText {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoskSpeechToText.class);

    private Model model;
    private Recognizer recognizer;

    @Value("${vosk.model.path}")
    private String modelPath;

    @Value("${vosk.sample.rate:16000}")
    private float sampleRate;

    @Override
    @PostConstruct
    public void init() throws IOException {
        // Load VOSK model from configured path
        model = new Model(modelPath);
        // Create recognizer with specified sample rate
        recognizer = new Recognizer(model, sampleRate);
    }

    @Override
    public String decodeAudio(InputStream audioStream) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;

        // Process audio stream in chunks
        while ((bytesRead = audioStream.read(buffer)) >= 0) {
            if (bytesRead > 0) {
                recognizer.acceptWaveForm(buffer, bytesRead);
            }
        }

        // Get final result and parse JSON
        String result = recognizer.getResult();
        return new JSONObject(result).getString("text");
    }

    @PreDestroy
    public void cleanup() {
        if (recognizer != null) {
            recognizer.close();
        }
        if (model != null) {
            model.close();
        }
    }
}