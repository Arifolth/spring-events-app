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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SentenceComparer {
    public static List<String> getSentences(String text) {
        // Simple sentence splitter using regex
        List<String> sentences = new ArrayList<>();
        Pattern pattern = Pattern.compile("[.!?]");
        String[] split = pattern.split(text);
        for (String sentence : split) {
            sentences.add(sentence.trim());
        }
        return sentences;
    }

    public static boolean haveSameStructure(String text1, String text2) {
        List<String> sentences1 = getSentences(text1);
        List<String> sentences2 = getSentences(text2);

        if (sentences1.size() != sentences2.size()) {
            return false;
        }

        for (int i = 0; i < sentences1.size(); i++) {
            if (!sentences1.get(i).equalsIgnoreCase(sentences2.get(i))) {
                return false;
            }
        }

        return true;
    }
}

