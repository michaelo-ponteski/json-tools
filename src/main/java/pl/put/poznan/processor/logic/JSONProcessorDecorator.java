package pl.put.poznan.processor.logic;

/**
 * Abstract decorator class for JSON processors.
 * Allows additional processing to be added to a JSONProcessor.
 */

public abstract class JSONProcessorDecorator implements JSONProcessor {
    /**
     * The wrapped JSONProcessor instance
     */
    protected JSONProcessor wrapper;
    /**
     * Constructs a JSONProcessorDecorator with the specified JSONProcessor to wrap.
     *
     * @param wrapper the JSONProcessor to wrap
     */
    public JSONProcessorDecorator(JSONProcessor wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * Delegates the JSON processing to the wrapped JSONProcessor.
     *
     * @param json the JSON string to process
     * @return the processed JSON string
     */

    @Override
    public String processJSON(String json) {
        return wrapper.processJSON(json);
    }
}