package pl.put.poznan.processor.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * A decorator for prettifying JSON strings.
 * Formats the JSON with proper indentation and structure.
 */
public class PrettifyDecorator extends JSONProcessorDecorator {
    /**
     * ObjectMapper instance for JSON parsing and writing.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructs a PrettifyDecorator with the specified JSONProcessor to wrap.
     *
     * @param wrapper the JSONProcessor to wrap
     */
    public PrettifyDecorator(JSONProcessor wrapper) {
        super(wrapper);
    }

    /**
     * Processes the JSON string by prettifying it.
     *
     * @param json the JSON string to process
     * @return the prettified JSON string
     */
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