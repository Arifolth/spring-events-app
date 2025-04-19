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

package com.arifolth.events.speech.utils;

import java.util.Arrays;
import java.util.List;

public class TextPreprocessor {
    public static List<String> preprocess(String text) {
        // Convert to lowercase
        text = text.toLowerCase();

        // Remove punctuation using regex
        text = text.replaceAll("-+", " ").replaceAll("[^a-zA-Z0-9\\s]", "");

        // Tokenize into words
        return Arrays.asList(text.split("\\s+"));
    }

    /**
     * Counts the number of words in the given text.
     *
     * @param text The input text.
     * @return The number of words.
     */
    public static int countWords(String text) {
        return preprocess(text).size();
    }
}