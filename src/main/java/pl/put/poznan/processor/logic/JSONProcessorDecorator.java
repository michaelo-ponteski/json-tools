package pl.put.poznan.processor.logic;


public abstract class JSONProcessorDecorator implements JSONProcessor {
    protected JSONProcessor wrapper;
    public JSONProcessorDecorator(JSONProcessor wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public String processJSON(String json) {
        return wrapper.processJSON(json);
    }
}