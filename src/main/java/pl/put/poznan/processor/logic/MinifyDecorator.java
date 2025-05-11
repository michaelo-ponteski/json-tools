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
        StringBuilder minifiedJson = new StringBuilder();
        boolean inQuotes = false;
        for (char c : processedJson.toCharArray()) {
            if (c == '\"') {
                inQuotes = !inQuotes;
            }
            if (!inQuotes && Character.isWhitespace(c)) {
                continue;
            }
            minifiedJson.append(c);
        }
        return minifiedJson.toString();
    }
}
