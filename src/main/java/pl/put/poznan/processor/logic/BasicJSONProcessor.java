package pl.put.poznan.processor.logic;


/**
 * A basic implementation of the JSONProcessor interface.
 * This processor performs no modifications to the input JSON.
 */
public class BasicJSONProcessor implements JSONProcessor {
    /**
     * Processes the given JSON string and returns it unchanged.
     *
     * @param json the JSON string to process
     * @return the unchanged JSON string
     */
    @Override
    public String processJSON(String json) {
        return json;
    }
}