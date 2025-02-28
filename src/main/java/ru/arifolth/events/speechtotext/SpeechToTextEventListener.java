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
import ru.arifolth.events.llmrunner.event.RunLLMEvent;
import ru.arifolth.events.speechtotext.event.SpeechToTextEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class SpeechToTextEventListener  extends ru.arifolth.events.EventListener<SpeechToTextEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SpeechToTextEventListener.class);

    @Autowired
    private VoskSpeechToText speechToText;

    public SpeechToTextEventListener() throws IOException {
    }

    public String transcribeAudio(String audioFile) throws IOException {
        Class clazz = VoskSpeechToText.class;
        try (InputStream audioStream = clazz.getResourceAsStream(audioFile);) {
            return speechToText.decodeAudio(audioStream);
        }
    }

    @Override
    protected void processEvent(SpeechToTextEvent event) {
        logger.info("STT processEvent()");

        try {
//            String transcription = transcribeAudio("/test.wav");
            //ffmpeg -i ana-ttsfree\(dot\)com.mp3 -acodec pcm_s16le -ac 1 -ar 16000 ana-ttsfree\(dot\)com.wav
            //wave PCM Mono 16000Hz
            String transcription = transcribeAudio("/ana-ttsfree(dot)com.wav");


            System.out.println("Transcription: " + transcription);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        publisher.publishEvent(new RunLLMEvent("Invent a Fairy tale about Pettson and Findus, as would do the writer Sven Nordqvist in his new novell titled: How Pettson and Findus repaired their car"));
    }
}
