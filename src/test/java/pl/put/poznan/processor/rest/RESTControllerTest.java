package pl.put.poznan.processor.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import pl.put.poznan.processor.logic.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RESTControllerTest {

    @Mock
    private Logger logger;

    @Mock
    private BasicJSONProcessor mockBasicProcessor;

    @Mock
    private MinifyDecorator mockMinifyDecorator;

    @Mock
    private PrettifyDecorator mockPrettifyDecorator;

    @Mock
    private KeyFilterDecorator mockFilterDecorator;

    @Mock
    private KeyExcludeDecorator mockExcludeDecorator;

    @Mock
    private TextCompareDecorator mockCompareDecorator;

    @Mock
    private CSVConverterDecorator mockCsvDecorator;

    @InjectMocks
    private RESTController restController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testMinify() {
        // Arrange
        String input = "{ \"name\": \"John\" }";
        String expected = "{\"name\":\"John\"}";
        when(mockMinifyDecorator.processJSON(input)).thenReturn(expected);

        // Act
        String result = restController.minify(input);

        // print expected and result for debugging
        System.out.println("Expected: " + expected);
        System.out.println("Result: " + result);
        // Assert
        assertEquals(expected, result);
        //verify(logger, times(1)).debug(contains("Minifying JSON"));
    }

    @Test
    void testPrettify() {
        // Arrange
        String input = "{\"name\":\"John\"}";
        String expected = "{" + System.lineSeparator() + "  \"name\" : \"John\"" + System.lineSeparator() + "}";
        when(mockPrettifyDecorator.processJSON(input)).thenReturn(expected);

        // Act
        String result = restController.prettify(input);

        // Assert
        assertEquals(expected, result);
        //verify(logger, times(1)).debug(contains("Prettifying JSON"));
    }

    @Test
    void testFilter() {
        // Arrange
        String input = "{ \"name\": \"John\", \"age\": 30 }";
        List<String> keys = Arrays.asList("name");
        String expected = "{\"name\":\"John\"}";
        when(mockFilterDecorator.processJSON(input)).thenReturn(expected);

        // Act
        String result = restController.filter(input, keys);

        // Assert
        assertEquals(expected, result);
        //verify(logger, times(1)).debug(contains("Filtering JSON"));
        //verify(logger, times(1)).debug(contains("Keys to keep"));
    }

    @Test
    void testExclude() {
        // Arrange
        String input = "{ \"name\": \"John\", \"age\": 30 }";
        List<String> keys = Arrays.asList("age");
        String expected = "{\"name\":\"John\"}";
        when(mockExcludeDecorator.processJSON(input)).thenReturn(expected);

        // Act
        String result = restController.exclude(input, keys);

        // Assert
        assertEquals(expected, result);
        //verify(logger, times(1)).debug(contains("Excluding keys"));
    }

    @Test
    void testCompare() {
        // Arrange
        String json1 = "{\"name\":\"John\",\"age\":30}";
        String json2 = "{\"name\":\"John\",\"age\":25}";
        String expected = "Differences between JSON's:\n\nLine 3:\nJSON 1: \"age\" : 30\nJSON 2: \"age\" : 25";
        when(mockCompareDecorator.processJSON(json1)).thenReturn(expected);

        // Act
        String result = restController.compare(json1, json2);

        // Assert
        assertEquals(expected, result);
        //verify(logger, times(1)).debug(contains("Comparing JSONs"));
    }

    @Test
    void testConvertToCSV() {
        // Arrange
        String input = "[{\"name\":\"John\",\"age\":30}]";
        String expected = "name,age\nJohn,30\n";
        when(mockCsvDecorator.processJSON(input)).thenReturn(expected);

        // Act
        String result = restController.convertToCSV(input);

        // Assert
        assertEquals(expected, result);
        //verify(logger, times(1)).debug(contains("Converting JSON to CSV"));
    }
}