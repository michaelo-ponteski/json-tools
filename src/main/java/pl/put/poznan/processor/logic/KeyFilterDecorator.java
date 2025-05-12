package pl.put.poznan.processor.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Iterator;

public class KeyFilterDecorator extends JSONProcessorDecorator {
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<String> keysToKeep;

    public KeyFilterDecorator(JSONProcessor wrapper, List<String> keysToKeep) {
        super(wrapper);
        this.keysToKeep = keysToKeep;
    }

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