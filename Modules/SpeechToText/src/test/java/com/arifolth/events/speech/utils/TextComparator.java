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

import java.io.IOException;
import java.util.List;

public class TextComparator {
    private SpellingErrorDetector detector;
    private float accuracyThreshold;

    /**
     * Constructor that initializes the comparator with a specified accuracy threshold.
     *
     * @param accuracyThreshold The accuracy threshold (0.0 to 1.0).
     */
    public TextComparator(float accuracyThreshold) {
        this.accuracyThreshold = accuracyThreshold;
        // Initialize the SpellingErrorDetector with the same accuracy as the threshold
        this.detector = new SpellingErrorDetector(accuracyThreshold);
    }

    /**
     * Compares the error rates of two texts based on the specified accuracy threshold.
     *
     * @param text1 The first text to compare.
     * @param text2 The second text to compare.
     * @return True if the error rate difference is within the accuracy threshold, else false.
     */
    public boolean compareTexts(String text1, String text2) {
        // Build the custom dictionary from text1
        detector.buildCustomDictionary(text1);

        // Get errors in text1
        List<String> errors1 = detector.getSpellingErrors(text1);
        int totalWords1 = TextPreprocessor.countWords(text1);
        float errorRate1 = (float) errors1.size() / totalWords1;

        // Get errors in text2
        List<String> errors2 = detector.getSpellingErrors(text2);
        int totalWords2 = TextPreprocessor.countWords(text2);
        float errorRate2 = (float) errors2.size() / totalWords2;

        // Calculate the difference in error rates
        float errorDifference = Math.abs(errorRate1 - errorRate2);

        // Compare with the accuracy threshold
        return errorDifference <= accuracyThreshold;
    }

    /**
     * Closes the spell checker and releases resources.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void close() throws IOException {
        detector.close();
    }
}
