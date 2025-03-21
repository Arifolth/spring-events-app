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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.arifolth.events.event.SpeechToTextEvent;
import ru.arifolth.events.event.TextToSpeechEvent;

@Component
public class TextToSpeechEventListener  extends ru.arifolth.events.EventListener<TextToSpeechEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextToSpeechEventListener.class);
    private TextToSpeech tts = new TextToSpeech();

    @Override
    protected void processEvent(TextToSpeechEvent event) {
        LOGGER.info("TTS processEvent()");

        tts.speak(event.getMessage());

        publisher.publishEvent(new SpeechToTextEvent(""));
    }
}
