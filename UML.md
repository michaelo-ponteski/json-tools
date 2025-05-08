classDiagram
class JSONProcessor {
<<interface>>
+processJSON(json) String
}

    class BasicJSONProcessor {
        +processJSON(json) String
    }

    class JSONProcessorDecorator {
        -wrappee: JSONProcessor
        +JSONProcessorDecorator(processor: JSONProcessor)
        +processJSON(json) String
    }

    class MinifyDecorator {
        +processJSON(json) String
    }

    class PrettifyDecorator {
        +processJSON(json) String
    }

    class KeyFilterDecorator {
        -keysToInclude: List<String>
        +setKeysToInclude(keys: List<String>) void
        +processJSON(json) String
    }

    class KeyExcludeDecorator {
        -keysToExclude: List<String>
        +setKeysToExclude(keys: List<String>) void
        +processJSON(json) String
    }

    class TextCompareDecorator {
        +processJSON(json) String
        +compareTexts(text1, text2) String
    }

    class RESTController {
        +getMinifiedJSON(json) String
        +getPrettifiedJSON(json) String
        +getFilteredJSON(json, keys) String
        +getExcludedJSON(json, keys) String
        +compareTexts(text1, text2) String
    }

    JSONProcessor <|.. BasicJSONProcessor
    JSONProcessor <|.. JSONProcessorDecorator
    JSONProcessorDecorator <|-- MinifyDecorator
    JSONProcessorDecorator <|-- PrettifyDecorator
    JSONProcessorDecorator <|-- KeyFilterDecorator
    JSONProcessorDecorator <|-- KeyExcludeDecorator
    JSONProcessorDecorator <|-- TextCompareDecorator
    RESTController --> JSONProcessor
