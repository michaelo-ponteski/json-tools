package pl.put.poznan.processor.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class PrettifyDecorator extends JSONProcessorDecorator {
    private final ObjectMapper mapper = new ObjectMapper();

    public PrettifyDecorator(JSONProcessor processor) {
        super(processor);
    }

    @Override
    public String processJSON(String json) {
        String processedJson = wrapper.processJSON(json);
        try {
            Object jsonObject = mapper.readValue(processedJson, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format", e);
        }
    }
}