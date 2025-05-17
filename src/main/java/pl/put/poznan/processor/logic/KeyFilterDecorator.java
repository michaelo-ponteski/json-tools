package pl.put.poznan.processor.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Iterator;
/**
 * Recursively excludes specified keys from the given JSON node.
 */
public class KeyFilterDecorator extends JSONProcessorDecorator {
    /**
     * ObjectMapper instance for JSON parsing and writing.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * List of keys to keep in the JSON object.
     */
    private final List<String> keysToKeep;

    /**
     * Constructs a KeyFilterDecorator with the specified JSONProcessor and keys to keep.
     *
     * @param wrapper the JSONProcessor to wrap
     * @param keysToKeep the list of keys to keep
     */
    public KeyFilterDecorator(JSONProcessor wrapper, List<String> keysToKeep) {
        super(wrapper);
        this.keysToKeep = keysToKeep;
    }
    /**
     * Processes the JSON string by filtering to keep only specified keys.
     *
     * @param json the JSON string to process
     * @return the processed JSON string with only specified keys kept
     */
    @Override
    public String processJSON(String json) {
        String processedJson = wrapper.processJSON(json);
        try {
            JsonNode rootNode = mapper.readTree(processedJson);
            filterKeys(rootNode);
            return mapper.writeValueAsString(rootNode);
        } catch (Exception e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    /**
     * Recursively filters the given JSON node to keep only specified keys.
     *
     * @param node the JSON node to process
     */
    private void filterKeys(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            Iterator<String> fieldNames = objectNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                if (!keysToKeep.contains(fieldName)) {
                    fieldNames.remove();
                }
            }
        } else if (node.isArray()) {
            for (JsonNode childNode : node) {
                filterKeys(childNode);
            }
        }
    }
}