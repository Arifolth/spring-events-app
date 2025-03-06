AI Agents chaining done through loose coupled Event-based Chain-Of-Responsibility 

### Setup
#### Speech To Text Model
Download appropriate model (for example [vosk-model-en-us-0.42-gigaspeech](https://alphacephei.com/vosk/models/vosk-model-en-us-0.42-gigaspeech.zip)) from https://alphacephei.com/vosk/models

Unpack it under ./models/ project folder 

Provide path in [application.properties](src/main/resources/application.properties) variable `vosk.model.path`

#### LLM
Download Tinyllama from (for example [tinyllama-1.1b-chat-v1.0.Q8_0.gguf](https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF/blob/main/tinyllama-1.1b-chat-v1.0.Q8_0.gguf))

Put GGUF file under ./models/ folder and provide path in [application.properties](src/main/resources/application.properties) variable `tinyllama.model.path`

### Build
`mvn clean package`

### Run
`mvn spring-boot:run`
