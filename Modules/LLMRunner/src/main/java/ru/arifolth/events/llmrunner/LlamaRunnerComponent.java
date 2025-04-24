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

package ru.arifolth.events.llmrunner;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.LlamaOutput;
import de.kherud.llama.ModelParameters;
import de.kherud.llama.args.MiroStat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.arifolth.events.components.IGenerativeAi;

@Component
public class LlamaRunnerComponent implements IGenerativeAi {
    private static final Logger LOGGER = LoggerFactory.getLogger(LlamaRunnerComponent.class);

    @Value("${tinyllama.model.path}")
    private String modelPath;

    /*
    *
    System Part (<|system|>): Contains instructions, guidelines, or context for the assistant's behavior.
    User Part (<|user|>): Contains the user's input or question.
    Assistant Part (<|assistant|>): Reserved for the assistant's response, which is generated based on the system and user inputs.
    * */
    private String promptDefault="<|system|>\r\n"
            + "<|user|>\r\n"
            + "{question} </s>\r\n"
            + "<|assistant|>";
    private LlamaModel llamaModel;

    @Override
    @PostConstruct
    public void init() {
        // Initialize the Llama model with your desired parameters
        ModelParameters params=new ModelParameters().setThreads(6)
                .setCtxSize(0)
                .setModel(modelPath); //load tinyllama model from provided path
        this.llamaModel = new LlamaModel(params);
    }

    @Override
    public String generateResponse(String question) {
        StringBuilder response  = new StringBuilder();

        String prompt = promptDefault
                .replace("{question}", question);
        String listAntiprompt="</s>,<|im_end|>,User:";

        InferenceParameters inferParams=new InferenceParameters(prompt)
                .setTemperature(1.0f)
                .setFrequencyPenalty(0.2F)
                .setMiroStat(MiroStat.V2)
                .setStopStrings(listAntiprompt.split("[,]"));

        for(LlamaOutput output: llamaModel.generate(inferParams))	{
            System.out.print(output.toString());

            response.append(output.toString());
        }
        //Add line separator
        System.out.print(System.lineSeparator());

        return response.toString();
    }

    @PreDestroy
    public void destroy() {
        llamaModel.close();
    }
}