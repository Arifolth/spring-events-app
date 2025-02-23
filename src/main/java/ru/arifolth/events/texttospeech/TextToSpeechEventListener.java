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

package ru.arifolth.events.texttospeech;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.arifolth.events.speechtotext.event.SpeechToTextEvent;
import ru.arifolth.events.texttospeech.event.TextToSpeechEvent;

@Component
public class TextToSpeechEventListener {
    private static final Logger logger = LoggerFactory.getLogger(TextToSpeechEventListener.class);

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostConstruct
    public void init() {
        logger.info("AiEventListener initialized");
    }

    @Async
    @EventListener
    public void onApplicationEvent(TextToSpeechEvent event) {
        try {
            logger.info("Event received: {}", event.getMessage());
        } catch (Exception e) {
            logger.error("Failed to handle event", e);
        }
    }
}
