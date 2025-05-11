package pl.put.poznan.processor.logic;


public abstract class JSONProcessorDecorator implements JSONProcessor {
    protected JSONProcessor wrappee;
    public JSONProcessorDecorator(JSONProcessor wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public String processJSON(String json) {
        return wrappee.processJSON(json);
    }
}