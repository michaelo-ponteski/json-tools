package pl.put.poznan.processor.logic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class MinifyDecorator extends JSONProcessorDecorator {
    private final ObjectMapper mapper = new ObjectMapper();

    public MinifyDecorator(JSONProcessor processor) {
        super(processor);
    }

    @Override
    public String processJSON(String json) {
        String processedJson = wrappee.processJSON(json);
        try {
            Object jsonObj = mapper.readValue(processedJson, Object.class);
            return mapper.writeValueAsString(jsonObj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error minifying JSON: " + e.getMessage(), e);
        }
    }
}
