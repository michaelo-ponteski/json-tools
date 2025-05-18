package pl.put.poznan.processor.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * A decorator for minifying JSON strings.
 * Removes unnecessary whitespace and formats the JSON into a compact representation.
 */
public class MinifyDecorator extends JSONProcessorDecorator {
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, false)
            .configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_MISSING_VALUES, false);
    /**
     * Constructs a MinifyDecorator with the specified JSONProcessor to wrap.
     *
     * @param processor the JSONProcessor to wrap
     */
    public MinifyDecorator(JSONProcessor processor) {
        super(processor);
    }

    /**
     * Processes the JSON string by minifying it.
     *
     * @param json the JSON string to process
     * @return the minified JSON string
     */
    @Override
    public String processJSON(String json) {
        String processedJson = wrapper.processJSON(json);
        try {
            Object jsonObject = mapper.readValue(processedJson, Object.class);
            return mapper.writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format", e);
        }
    }


}