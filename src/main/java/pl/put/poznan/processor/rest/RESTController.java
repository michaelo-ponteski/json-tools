package pl.put.poznan.processor.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.processor.logic.*;

import java.util.List;

@RestController
@RequestMapping("/api/json")
public class RESTController {

    private static final Logger logger = LoggerFactory.getLogger(RESTController.class);

    // Minify JSON
    @PostMapping(value = "/minify", produces = MediaType.APPLICATION_JSON_VALUE)
    public String minify(@RequestBody String json) {
        logger.debug("Minifying JSON: {}", json);
        JSONProcessor processor = new MinifyDecorator(new BasicJSONProcessor());
        return processor.processJSON(json);
    }

    // Prettify JSON
//    @PostMapping(value = "/prettify", produces = MediaType.APPLICATION_JSON_VALUE)
//    public String prettify(@RequestBody String json) {
//        logger.debug("Prettifying JSON: {}", json);
//        JSONProcessor processor = new PrettifyDecorator(new BasicJSONProcessor());
//        return processor.processJSON(json);
//    }
//
//    // Filter JSON by keys
//    @PostMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
//    public String filter(@RequestBody String json, @RequestParam List<String> keys) {
//        logger.debug("Filtering JSON: {} with keys: {}", json, keys);
//        KeyFilterDecorator processor = new KeyFilterDecorator(new BasicJSONProcessor());
//        processor.setKeysToInclude(keys);
//        return processor.processJSON(json);
//    }
//
//    // Exclude keys from JSON
//    @PostMapping(value = "/exclude", produces = MediaType.APPLICATION_JSON_VALUE)
//    public String exclude(@RequestBody String json, @RequestParam List<String> keys) {
//        logger.debug("Excluding keys: {} from JSON: {}", keys, json);
//        KeyExcludeDecorator processor = new KeyExcludeDecorator(new BasicJSONProcessor());
//        processor.setKeysToExclude(keys);
//        return processor.processJSON(json);
//    }
//
//    // Compare two JSON texts
//    @PostMapping(value = "/compare", produces = MediaType.APPLICATION_JSON_VALUE)
//    public String compare(@RequestParam String text1, @RequestParam String text2) {
//        logger.debug("Comparing JSON texts.");
//        TextCompareDecorator processor = new TextCompareDecorator(new BasicJSONProcessor());
//        return processor.compareTexts(text1, text2);
//    }
}
