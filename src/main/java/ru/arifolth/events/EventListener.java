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

package ru.arifolth.events;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class EventListener<T extends Event> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventListener.class);
    private Class<T> eventType;

    @Autowired
    protected ApplicationEventPublisher publisher;

    @PostConstruct
    public void init() {
        // Determine generic type at runtime
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        this.eventType = (Class<T>) paramType.getActualTypeArguments()[0];
        LOGGER.info("EventListener initialized for {}", eventType.getSimpleName());
    }

    @Async
    @org.springframework.context.event.EventListener
    public void onApplicationEvent(Event event) {
        try {
            // Runtime type check
            if (eventType.isInstance(event)) {
                T typedEvent = eventType.cast(event);
                LOGGER.trace("Event received: {}", typedEvent.getMessage());
                processEvent(typedEvent);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to handle event", e);
        }
    }

    protected abstract void processEvent(T event);
}