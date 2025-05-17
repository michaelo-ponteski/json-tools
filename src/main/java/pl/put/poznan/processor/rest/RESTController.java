package pl.put.poznan.processor.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.processor.logic.*;

import java.util.List;

/**
 * REST controller for handling JSON processing requests.
 * Provides endpoints for various JSON operations such as minify, prettify, filter, exclude, and compare.
 */
@RestController
@RequestMapping("/api/json")
public class RESTController {

    private static final Logger logger = LoggerFactory.getLogger(RESTController.class);

    /**
     * Minifies the given JSON string.
     *
     * @param json the JSON string to minify
     * @return the minified JSON string
     */
    @PostMapping(value = "/minify", produces = MediaType.APPLICATION_JSON_VALUE)
    public String minify(@RequestBody String json) {
        logger.debug("Minifying JSON: {}", json);
        JSONProcessor processor = new MinifyDecorator(new BasicJSONProcessor());
        return processor.processJSON(json);
    }

    /**
     * Prettifies the given JSON string.
     *
     * @param json the JSON string to prettify
     * @return the prettified JSON string
     */
    @PostMapping(value = "/prettify", produces = MediaType.APPLICATION_JSON_VALUE)
    public String prettify(@RequestBody String json) {
        logger.debug("Prettifying JSON: {}", json);
        JSONProcessor processor = new PrettifyDecorator(new BasicJSONProcessor());
        return processor.processJSON(json);
    }

    /**
     * Filters the given JSON string to keep only specified keys.
     *
     * @param json the JSON string to filter
     * @param keys the list of keys to keep
     * @return the filtered JSON string
     */
    @PostMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public String filter(@RequestBody String json, @RequestParam List<String> keys) {
        logger.debug("Filtering JSON: {}", json);
        logger.debug("Keys to keep: {}", keys);
        JSONProcessor processor = new KeyFilterDecorator(new BasicJSONProcessor(), keys);
        return processor.processJSON(json);
    }

    /**
     * Excludes specified keys from the given JSON string.
     *
     * @param json the JSON string to process
     * @param keys the list of keys to exclude
     * @return the JSON string with specified keys excluded
     */
    @PostMapping(value = "/exclude", produces = MediaType.APPLICATION_JSON_VALUE)
    public String exclude(@RequestBody String json, @RequestParam List<String> keys) {
        logger.debug("Excluding keys from JSON: {}", json);
        logger.debug("Keys to exclude: {}", keys);
        JSONProcessor processor = new KeyExcludeDecorator(new BasicJSONProcessor(), keys);
        return processor.processJSON(json);
    }

    /**
     * Compares two JSON strings and identifies differences.
     *
     * @param json1 the first JSON string
     * @param json2 the second JSON string
     * @return a string describing the differences between the two JSONs
     */
    @PostMapping(value = "/compare", produces = MediaType.TEXT_PLAIN_VALUE)
    public String compare(@RequestParam String json1, @RequestParam String json2) {
        logger.debug("Comparing JSONs: {} and {}", json1, json2);
        JSONProcessor processor = new TextCompareDecorator(new BasicJSONProcessor(), json2);
        return processor.processJSON(json1);
    }
}
