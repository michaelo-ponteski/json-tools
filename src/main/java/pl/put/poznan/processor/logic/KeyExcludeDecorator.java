package pl.put.poznan.processor.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Iterator;

/**
 * A decorator for excluding specific keys from a JSON object.
 * Extends the functionality of a JSONProcessor by removing specified keys.
 */
public class KeyExcludeDecorator extends JSONProcessorDecorator {
    /**
     * ObjectMapper instance for JSON parsing and writing.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * List of keys to exclude from the JSON object.
     */
    private final List<String> keysToExclude;

    /**
     * Constructs a KeyExcludeDecorator with the specified JSONProcessor and keys to exclude.
     *
     * @param wrapper the JSONProcessor to wrap
     * @param keysToExclude the list of keys to exclude
     */
    public KeyExcludeDecorator(JSONProcessor wrapper, List<String> keysToExclude) {
        super(wrapper);
        this.keysToExclude = keysToExclude;
    }

    /**
     * Processes the JSON string by excluding specified keys.
     *
     * @param json the JSON string to process
     * @return the processed JSON string with specified keys excluded
     */
    @Override
    public String processJSON(String json) {
        String processedJson = wrapper.processJSON(json);
        try {
            JsonNode rootNode = mapper.readTree(processedJson);
            excludeKeys(rootNode);
            return mapper.writeValueAsString(rootNode);
        } catch (Exception e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    /**
     * Recursively excludes specified keys from the given JSON node.
     *
     * @param node the JSON node to process
     */
    private void excludeKeys(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<String> fieldNames = objectNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                if (keysToExclude.contains(fieldName)) {
                    fieldNames.remove();
                }
            }
        } else if (node.isArray()) {
            for (JsonNode childNode : node) {
                excludeKeys(childNode);
            }
        }
    }
}