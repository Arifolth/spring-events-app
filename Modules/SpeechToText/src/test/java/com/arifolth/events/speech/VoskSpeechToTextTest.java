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

package com.arifolth.events.speech;

import com.arifolth.events.speech.utils.TextComparator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.arifolth.events.speechtotext.AudioService;
import ru.arifolth.events.speechtotext.VoskSpeechToText;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {VoskSpeechToText.class, FileContentLoader.class})
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class VoskSpeechToTextTest {
    private static final Logger logger = LoggerFactory.getLogger(VoskSpeechToTextTest.class);

    @Autowired
    private FileContentLoader fileContentLoader;

    @Autowired
    private VoskSpeechToText voskSpeechToText;

    private InputStream audioStream;

    @BeforeEach
    void setUp() throws IOException {
        // Load a sample audio file
        ClassPathResource resource = new ClassPathResource("audio/TTS_832443.wav"); // Place a ".wav" in src/test/resources
        audioStream = resource.getInputStream();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (audioStream != null) {
            audioStream.close();
        }
    }


    @Test
    void testDecodeAudio_validInput() throws IOException, org.json.JSONException {
        TextComparator comparator = new TextComparator(0.15f);

        String result = voskSpeechToText.decodeAudio(audioStream);
        assertNotNull(result);
        logger.info(result);
        boolean areSimilar = comparator.compareTexts(fileContentLoader.getFileContent(), result);

        assertTrue(areSimilar);
    }

    @Test
    void testDecodeAudio_emptyInputStream() throws IOException {
        try (InputStream emptyStream = new ByteArrayInputStream(new byte[0])) {
            String result = voskSpeechToText.decodeAudio(emptyStream);
            assertEquals("", result); // Expect an empty string if the input is empty
        }
    }

    @Test
    void testDecodeAudio_invalidJson() throws IOException, org.json.JSONException {
        // Simulate a scenario where Vosk returns invalid JSON (e.g., by modifying the recognizer's behavior - difficult to do directly)
        // This is more of an integration/system test than a pure unit test, but it's important.

        try (InputStream corruptedStream = new ByteArrayInputStream(new byte[0])) { //Dummy stream for now
            String result = voskSpeechToText.decodeAudio(corruptedStream);
            assertEquals("",result);
        }
    }


    @Test
    @Order(Integer.MAX_VALUE)
    void testInit_modelNotFound() throws IOException {
        try {
            // Temporarily change the model path to a non-existent location.  This will trigger an exception during initialization.
            Field modelPathField = VoskSpeechToText.class.getDeclaredField("modelPath");
            modelPathField.setAccessible(true);

            String originalModelPath = (String) modelPathField.get(voskSpeechToText);

            modelPathField.set(voskSpeechToText, "non/existent/model.zip");
            try {
                voskSpeechToText.init(); // Should throw an IOException
                fail("Expected IOException to be thrown when model is not found.");
            } catch (IOException e) {
                // Expected exception - verify the message if needed.
                assertEquals("Failed to create a model",e.getMessage());
            } finally {
                modelPathField.set(voskSpeechToText, originalModelPath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to set modelPath", e);
        }
    }

    @Test
    void testCleanup() throws IOException{
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        String result = voskSpeechToText.decodeAudio(stream);
        voskSpeechToText.cleanup();
    }
}
