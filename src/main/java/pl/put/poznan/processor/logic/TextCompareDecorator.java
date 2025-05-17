package pl.put.poznan.processor.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A decorator for comparing two JSON strings.
 * Compares the input JSON with another JSON and identifies differences.
 */
public class TextCompareDecorator extends JSONProcessorDecorator {
    /**
     * ObjectMapper instance for JSON parsing and writing.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * The JSON string to compare against.
     */
    private final String jsonToCompare;

    /**
     * Constructs a TextCompareDecorator with the specified JSONProcessor and JSON to compare.
     *
     * @param wrapper the JSONProcessor to wrap
     * @param jsonToCompare the JSON string to compare against
     */
    public TextCompareDecorator(JSONProcessor wrapper, String jsonToCompare) {
        super(wrapper);
        this.jsonToCompare = jsonToCompare;
    }

    /**
     * Processes the JSON string by comparing it with another JSON.
     *
     * @param json the JSON string to process
     * @return a string describing the differences between the two JSONs
     */
    @Override
    public String processJSON(String json) {
        return compareJSON(json, jsonToCompare);
    }

    /**
     * Compares two JSON strings and identifies differences.
     *
     * @param text1 the first JSON string
     * @param text2 the second JSON string
     * @return a string describing the differences
     */
    private String compareJSON(String text1, String text2) {
        if ((text1 == null || text1.trim().isEmpty()) && (text2 == null || text2.trim().isEmpty())) {
            return "Both JSON's are empty";
        }
        if (text1 == null || text1.trim().isEmpty()) {
            return "First JSON is empty\n\nSecond JSON:\n" + text2;
        }
        if (text2 == null || text2.trim().isEmpty()) {
            return "Second JSON is empty\n\nFirst JSON:\n" + text1;
        }

        try {
            Object json1 = mapper.readValue(text1, Object.class);
            Object json2 = mapper.readValue(text2, Object.class);

            String jsonPretty1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json1);
            String jsonPretty2 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json2);

            List<String> differences = getDifferences(jsonPretty1, jsonPretty2);

            if (differences.isEmpty()) {
                return "No differences between JSON's";
            } else {
                return "Differences between JSON's:\n\n" + String.join("\n\n", differences);
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Identifies the differences between two JSON strings line by line.
     *
     * @param json1 the first JSON string
     * @param json2 the second JSON string
     * @return a list of strings describing the differences between the two JSON strings
     */
    private List<String> getDifferences(String json1, String json2) {
        List<String> differences = new ArrayList<>();
        List<String> jsonLines1 = Arrays.asList(json1.split("\n"));
        List<String> jsonLines2 = Arrays.asList(json2.split("\n"));
        int minLen = Math.min(jsonLines1.size(), jsonLines2.size());

        for (int i = 0; i < minLen; i++) {
            String line1 = jsonLines1.get(i).trim();
            String line2 = jsonLines2.get(i).trim();

            if (!line1.equals(line2)) {
                differences.add("Line " + (i + 1) + ":\nJSON 1: " + line1 + "\nJSON 2: " + line2);
            }
        }

        if (jsonLines1.size() > minLen) {
            for (int i = minLen; i < jsonLines1.size(); i++) {
                differences.add("Line " + (i + 1) + ":\nJSON 1: " + jsonLines1.get(i).trim() + "\nJSON 2: <empty>");
            }
        } else if (jsonLines2.size() > minLen) {
            for (int i = minLen; i < jsonLines2.size(); i++) {
                differences.add("Line " + (i + 1) + ":\nJSON 1: <empty>\nJSON 2: " + jsonLines2.get(i).trim());
            }
        }
        return differences;
    }
}
