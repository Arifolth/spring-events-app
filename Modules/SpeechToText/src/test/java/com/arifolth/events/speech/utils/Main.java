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

public class Main {


    public static void main(String[] args) {
        String text1 = "Pettson is an old farmer who lives in a ramshackle falu red-painted wooden farmhouse in the Swedish countryside...";
        String text2 = "";


        double similarity = TextComparator.similarityPercentage(text1, text2);
        System.out.printf("Similarity: %.2f%%\n", similarity);

    }
}
