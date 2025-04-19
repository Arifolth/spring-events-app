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

import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.*;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SpellingErrorDetector {
    private SpellChecker spellChecker;
    private Directory spellIndexDirectory;

    public SpellingErrorDetector(float accuracyThreshold) {
        try {
            // Create a temporary directory for the spell checker index
            Path tempDir = Files.createTempDirectory("lucene-spellchecker");
            spellIndexDirectory = FSDirectory.open(tempDir);

            // Initialize the spell checker
            spellChecker = new SpellChecker(spellIndexDirectory);
            spellChecker.setStringDistance(new LevenshteinDistance());
            spellChecker.setAccuracy(accuracyThreshold); //100% hit
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Builds a custom dictionary from the given text and indexes it.
     *
     * @param text The input text to build the custom dictionary from.
     */
    public void buildCustomDictionary(String text) {
        try {
            // Preprocess the text: convert to lowercase and remove punctuation
            String processedText = text.toLowerCase().replaceAll("-+", " ").replaceAll("[^a-zA-Z\\s]", "");

            // Split into words
            List<String> words = Arrays.asList(processedText.split("\\s+"));

            // Remove duplicates
            Set<String> uniqueWords = new HashSet<>(words);

            // Create a temporary file for the custom dictionary
            Path customDictionaryFile = Files.createTempFile("custom-dictionary", ".txt");
            try (BufferedWriter writer = Files.newBufferedWriter(customDictionaryFile, StandardCharsets.UTF_8)) {
                for (String word : uniqueWords) {
                    if (isWordLength(word)) {
                        writer.write(word);
                        writer.newLine();
                    }
                }
            }

            // Use FileDictionary with an InputStream
            try (InputStream is = new FileInputStream(customDictionaryFile.toFile())) {
                Dictionary customDictionary = new PlainTextDictionary(is);
                spellChecker.indexDictionary(customDictionary, new IndexWriterConfig(), false);
            } finally {
                // Clean up the temporary custom dictionary file
                Files.delete(customDictionaryFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Detects spelling errors in the given text.
     *
     * @param text The input text to check.
     * @return A list of misspelled words.
     */
    public List<String> getSpellingErrors(String text) {
        List<String> errors = new ArrayList<>();
        List<String> words = TextPreprocessor.preprocess(text);

        for (String word : words) {
            try {
                if (!spellChecker.exist(word) && isWordLength(word) && !isCommonTypo(word)) {
                    errors.add(word);
                }
            } catch (IOException e) {
                e.printStackTrace();
                // If an error occurs, assume the word is misspelled
                errors.add(word);
            }
        }

        return errors;
    }

    private static boolean isWordLength(String word) {
        return word.length() > 2;
    }

    /**
     * Optional: Handles common typos or specific cases.
     *
     * @param word The word to check.
     * @return True if the word is a common typo, else false.
     */
    private boolean isCommonTypo(String word) {
        // Example: Treat single-letter words as typos
        if (word.length() == 1) {
            return true;
        }
        // Add more rules as needed
        return false;
    }

    /**
     * Closes the spell checker and releases resources.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void close() throws IOException {
        spellChecker.close();
        spellIndexDirectory.close();
    }
}
