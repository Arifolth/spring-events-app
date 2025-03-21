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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.arifolth.events.event.RunLLMEvent;
import ru.arifolth.events.event.SpeechToTextEvent;

import java.io.IOException;
import java.io.InputStream;

@Component
public class SpeechToTextEventListener  extends ru.arifolth.events.EventListener<SpeechToTextEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpeechToTextEventListener.class);

    @Autowired
    private AudioService audioService;
    @Autowired
    private VoskSpeechToText speechToText;

    public SpeechToTextEventListener() throws IOException {
    }

    public String transcribeAudio(String audioFile) throws IOException {
        Class clazz = VoskSpeechToText.class;
        try (InputStream audioStream = clazz.getResourceAsStream(audioFile)) {
            return speechToText.decodeAudio(audioStream);
        }
    }

    public String transcribeAudio(InputStream audioStream) throws IOException {
        return speechToText.decodeAudio(audioStream);
    }

    @Override
    protected void processEvent(SpeechToTextEvent event) {
        LOGGER.info("STT processEvent()");

        try {
//            String transcription = transcribeAudio("/test.wav");
            //ffmpeg -i ana-ttsfree\(dot\)com.mp3 -acodec pcm_s16le -ac 1 -ar 16000 ana-ttsfree\(dot\)com.wav
            //wave PCM Mono 16000Hz
            InputStream audioStream = audioService.captureAudio(15000);
            String transcription = transcribeAudio(audioStream);

            LOGGER.info("Transcription: " + transcription);

            publisher.publishEvent(new RunLLMEvent(transcription));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
