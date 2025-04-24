/*
 * Copyright 2025 Alexander Nilov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.arifolth.events.speechtotext;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;
import ru.arifolth.events.components.ISpeechToText;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class VoskSpeechToText implements ISpeechToText {
    private static final Logger LOGGER = LoggerFactory.getLogger(VoskSpeechToText.class);
    public static final String EMPTY_STRING = "";

    private Model model;
    private Recognizer recognizer;

    @Value("${vosk.model.path}")
    private String modelPath;

    @Value("${vosk.sample.rate:16000}")
    private float sampleRate;

    @Override
    @PostConstruct
    public void init() throws IOException {
        LibVosk.setLogLevel(LogLevel.INFO);
        // Load VOSK model from configured path
        model = new Model(modelPath);
        // Create recognizer with specified sample rate
        recognizer = new Recognizer(model, sampleRate);
    }

    public boolean isValidInputStream(InputStream inputStream) {
        if (inputStream == null) {
            LOGGER.error("InputStream is null.");
            return false;
        }
        try {
            if (inputStream.available() == 0) {
                LOGGER.error("InputStream is empty.");
                return false;
            }
        } catch (IOException e) {
            LOGGER.error("Error checking InputStream availability: " + e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public String decodeAudio(InputStream audioStream) throws IOException, JSONException {
        if (!isValidInputStream(audioStream))
            return EMPTY_STRING;

        audioStream = new BufferedInputStream(audioStream);
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