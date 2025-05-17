package pl.put.poznan.processor.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextCompareDecorator extends JSONProcessorDecorator {
    private final ObjectMapper mapper = new ObjectMapper();
    private final String jsonToCompare;

    public TextCompareDecorator(JSONProcessor processor, String jsonToCompare) {
        super(processor);
        this.jsonToCompare = jsonToCompare;
    }

    @Override
    public String processJSON(String json) {
        return compareJSON(json, jsonToCompare);
    }

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
