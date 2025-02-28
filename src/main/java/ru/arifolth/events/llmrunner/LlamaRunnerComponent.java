package ru.arifolth.events.llmrunner;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.LlamaOutput;
import de.kherud.llama.ModelParameters;
import de.kherud.llama.args.MiroStat;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class LlamaRunnerComponent {
    private String promptDefault="<|system|>\r\n"
            + "<|user|>\r\n"
            + "{question} </s>\r\n"
            + "<|assistant|>";
    private LlamaModel llamaModel;

    public LlamaRunnerComponent() {
        // Initialize the Llama model with your desired parameters
        ModelParameters params=new ModelParameters().setNThreads(2)
                .setNCtx(0)
                .setModelFilePath("tinyllama-1.1b-chat-v1.0.Q8_0.gguf");
        this.llamaModel = new LlamaModel(params);
    }

    public String generateResponse(String question) {
        StringBuilder response  = new StringBuilder();

        String prompt = promptDefault
                .replace("{question}", question);
        String listAntiprompt="</s>,<|im_end|>,User:";

        InferenceParameters inferParams=new InferenceParameters(prompt)
                .setTemperature(0.8f)
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