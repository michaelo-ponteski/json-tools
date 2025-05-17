package pl.put.poznan.processor.logic;

/**
 * Interface representing a JSON processor.
 * Provides a method to process JSON data.
 */

public interface JSONProcessor {
    /**
     * Processes the given JSON string and returns the processed result.
     *
     * @param json the JSON string to process
     * @return the processed JSON string
     */
    String processJSON(String json);
}
