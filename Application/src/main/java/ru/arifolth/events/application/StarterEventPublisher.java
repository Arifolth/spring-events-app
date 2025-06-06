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

package ru.arifolth.events.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import ru.arifolth.events.event.SpeechToTextEvent;

import java.awt.*;

@Component
public class StarterEventPublisher {
    @Autowired
    private ApplicationEventPublisher publisher;

    public void publishCustomEvent() {
        Toolkit.getDefaultToolkit().beep();

        publisher.publishEvent(new SpeechToTextEvent(""));
    }
}