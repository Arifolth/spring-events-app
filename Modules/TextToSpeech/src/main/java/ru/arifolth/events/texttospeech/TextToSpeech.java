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

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import ru.arifolth.events.components.ITextToSpeech;

public class TextToSpeech implements ITextToSpeech {
    private Voice voice;

    public TextToSpeech(){
        voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice == null) {
            throw new RuntimeException("Cannot find the 'kevin' voice.");
        }
        // This will not return until TTS has been quit
        voice.allocate();
    }

    @Override
    public void speak(String text){
        voice.speak(text);
    }
}
