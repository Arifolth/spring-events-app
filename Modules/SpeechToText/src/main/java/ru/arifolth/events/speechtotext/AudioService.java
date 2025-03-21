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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class AudioService {
    private static final Logger logger = LoggerFactory.getLogger(AudioService.class);

    public AudioInputStream captureAudio(int durationInMillis) throws IOException {
        // Define audio format
        AudioFormat format = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                16000, // Sample Rate
                16,    // Bits per sample
                1,     // Channels (mono)
                2,     // Frame size in bytes (bits per sample * channels / 8)
                16000, // Frame rate
                false  // Little endian
        );

        // Get the microphone
        TargetDataLine microphone = null;
        try {
            microphone = AudioSystem.getTargetDataLine(format);
            microphone.open(format);
            microphone.start();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        // Capture audio data
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        long startTime = System.currentTimeMillis();

        logger.info("Starting audio capture...");
        while ((bytesRead = microphone.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, bytesRead);
            if (System.currentTimeMillis() - startTime > durationInMillis) {
                break;
            }
        }
        logger.info("Audio capture completed.");

        // Stop and close the microphone
        microphone.stop();
        microphone.close();

        // Convert bytes to audio input stream
        byte[] audioData = out.toByteArray();
        AudioInputStream audioInputStream = new AudioInputStream(
                new ByteArrayInputStream(audioData),
                format,
                audioData.length / format.getFrameSize()
        );

        return audioInputStream;
    }
}