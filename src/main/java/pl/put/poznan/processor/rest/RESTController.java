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
    @PostMapping(value = "/prettify", produces = MediaType.APPLICATION_JSON_VALUE)
    public String prettify(@RequestBody String json) {
        logger.debug("Prettifying JSON: {}", json);
        JSONProcessor processor = new PrettifyDecorator(new BasicJSONProcessor());
        return processor.processJSON(json);
    }


}
