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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BasicContentComparer {
    public static boolean haveSameContent(String text1, String text2) {
        List<String> words1 = TextPreprocessor.preprocess(text1);
        List<String> words2 = TextPreprocessor.preprocess(text2);

        // Remove duplicates and sort
        Set<String> set1 = new HashSet<>(words1);
        Set<String> set2 = new HashSet<>(words2);

        return set1.equals(set2);
    }
}
