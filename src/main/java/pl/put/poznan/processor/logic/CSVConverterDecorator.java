package pl.put.poznan.processor.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.*;

public class CSVConverterDecorator extends JSONProcessorDecorator {

    public CSVConverterDecorator(JSONProcessor processor) {
        super(processor);
    }

    @Override
    public String processJSON(String json) {
        // First, process the JSON with the wrapped processor
        String processedJson = wrapper.processJSON(json);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(processedJson);

            // Check if input is a JSON array
            if (!rootNode.isArray()) {
                throw new RuntimeException("Input must be a JSON array of objects");
            }

            ArrayNode arrayNode = (ArrayNode) rootNode;
            if (arrayNode.size() == 0) {
                return ""; // Empty CSV for empty array
            }

            // Extract and order fields by analyzing the first object's structure
            List<String> orderedFields = new ArrayList<>();
            List<Map<String, String>> flattenedObjects = new ArrayList<>();

            // First, determine field order from the first object
            if (arrayNode.size() > 0 && arrayNode.get(0).isObject()) {
                extractOrderedFieldNames("", arrayNode.get(0), orderedFields);
            }

            // Now process all objects with the established field order
            for (JsonNode element : arrayNode) {
                if (!element.isObject()) {
                    throw new RuntimeException("Each element in the array must be a JSON object");
                }

                Map<String, String> flattenedObject = new HashMap<>();
                flattenJsonObject("", element, flattenedObject);
                flattenedObjects.add(flattenedObject);

                // Add any fields that weren't in the first object but are in others
                for (String field : flattenedObject.keySet()) {
                    if (!orderedFields.contains(field)) {
                        orderedFields.add(field);
                    }
                }
            }

            // Build CSV
            StringBuilder csv = new StringBuilder();

            // Add headers
            for (int i = 0; i < orderedFields.size(); i++) {
                csv.append(escapeCSVField(orderedFields.get(i)));
                if (i < orderedFields.size() - 1) {
                    csv.append(",");
                }
            }
            csv.append("\n");

            // Add data rows
            for (Map<String, String> obj : flattenedObjects) {
                for (int i = 0; i < orderedFields.size(); i++) {
                    String value = obj.getOrDefault(orderedFields.get(i), "");
                    csv.append(escapeCSVField(value));
                    if (i < orderedFields.size() - 1) {
                        csv.append(",");
                    }
                }
                csv.append("\n");
            }

            return csv.toString();

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format: " + e.getMessage());
        }
    }

    // New method to extract field names in order of appearance
    private void extractOrderedFieldNames(String prefix, JsonNode jsonNode, List<String> orderedFields) {
        if (jsonNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();
                JsonNode value = field.getValue();

                String newPrefix = prefix.isEmpty() ? key : prefix + "." + key;

                if (value.isObject()) {
                    extractOrderedFieldNames(newPrefix, value, orderedFields);
                } else {
                    orderedFields.add(newPrefix);
                }
            }
        }
    }

    private void flattenJsonObject(String prefix, JsonNode jsonNode, Map<String, String> flattenedMap) {
        if (jsonNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();
                JsonNode value = field.getValue();

                String newPrefix = prefix.isEmpty() ? key : prefix + "." + key;

                if (value.isObject()) {
                    flattenJsonObject(newPrefix, value, flattenedMap);
                } else if (value.isArray()) {
                    // For arrays, we'll stringify them
                    flattenedMap.put(newPrefix, value.toString());
                } else if (value.isNull()) {
                    flattenedMap.put(newPrefix, "");
                } else {
                    flattenedMap.put(newPrefix, value.asText());
                }
            }
        }
    }

    private String escapeCSVField(String field) {
        if (field == null) {
            return "";
        }

        // Check if field contains special characters that need escaping
        if (field.contains("\"") || field.contains(",") || field.contains("\n")) {
            // Replace double quotes with two double quotes and surround with quotes
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }

        return field;
    }
}