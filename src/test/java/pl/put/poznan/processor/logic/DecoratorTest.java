package pl.put.poznan.processor.logic;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DecoratorTest {

    @Mock
    private JSONProcessor mockProcessor;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testMinifyDecorator() {
        // Arrange
        String input = "{\n  \"name\": \"John\"\n}";
        String expected = "{\"name\":\"John\"}";
        when(mockProcessor.processJSON(input)).thenReturn(input);
        MinifyDecorator decorator = new MinifyDecorator(mockProcessor);

        // Act
        String result = decorator.processJSON(input);

        // Assert
        assertEquals(expected, result);
        verify(mockProcessor, times(1)).processJSON(input);
    }

    @Test
    void testPrettifyDecorator() {
        // Arrange
        String input = "{\"name\":\"John\"}";
        when(mockProcessor.processJSON(input)).thenReturn(input);
        PrettifyDecorator decorator = new PrettifyDecorator(mockProcessor);

        // Act
        String result = decorator.processJSON(input);

        // Assert
        assertTrue(result.contains("\n"));
        assertTrue(result.contains("  "));
        verify(mockProcessor, times(1)).processJSON(input);
    }

    @Test
    void testKeyFilterDecorator() {
        // Arrange
        String input = "{\"name\":\"John\",\"age\":30}";
        List<String> keys = Arrays.asList("name");
        when(mockProcessor.processJSON(input)).thenReturn(input);
        KeyFilterDecorator decorator = new KeyFilterDecorator(mockProcessor, keys);

        // Act
        String result = decorator.processJSON(input);

        // Assert
        assertTrue(result.contains("\"name\""));
        assertFalse(result.contains("\"age\""));
        verify(mockProcessor, times(1)).processJSON(input);
    }

    @Test
    void testKeyExcludeDecorator() {
        // Arrange
        String input = "{\"name\":\"John\",\"age\":30}";
        List<String> keys = Arrays.asList("age");
        when(mockProcessor.processJSON(input)).thenReturn(input);
        KeyExcludeDecorator decorator = new KeyExcludeDecorator(mockProcessor, keys);

        // Act
        String result = decorator.processJSON(input);

        // Assert
        assertTrue(result.contains("\"name\""));
        assertFalse(result.contains("\"age\""));
        verify(mockProcessor, times(1)).processJSON(input);
    }

    @Test
    void testTextCompareDecorator() {
        // Arrange
        String json1 = "{\"name\":\"John\",\"age\":30}";
        String json2 = "{\"name\":\"John\",\"age\":25}";
        when(mockProcessor.processJSON(json1)).thenReturn(json1);
        TextCompareDecorator decorator = new TextCompareDecorator(mockProcessor, json2);

        // Act
        String result = decorator.processJSON(json1);

        // Assert
        assertTrue(result.contains("age"));
        assertTrue(result.contains("30"));
        assertTrue(result.contains("25"));
        verify(mockProcessor, times(1)).processJSON(json1);
    }

    @Test
    void testCSVConverterDecoratorBasic() {
        // Arrange
        String input = "[{\"name\":\"John\",\"age\":30}]";
        when(mockProcessor.processJSON(input)).thenReturn(input);
        CSVConverterDecorator decorator = new CSVConverterDecorator(mockProcessor);

        // Act
        String result = decorator.processJSON(input);

        // Assert
        assertTrue(result.contains("name,age") || result.contains("age,name"));
        assertTrue(result.contains("John"));
        assertTrue(result.contains("30"));
        verify(mockProcessor, times(1)).processJSON(input);
    }

    @Test
    void testCSVConverterDecoratorWithNestedObjects() {
        // Arrange
        String input = "[{\"name\":\"John\",\"address\":{\"city\":\"New York\"}}]";
        when(mockProcessor.processJSON(input)).thenReturn(input);
        CSVConverterDecorator decorator = new CSVConverterDecorator(mockProcessor);

        // Act
        String result = decorator.processJSON(input);

        // Assert
        assertTrue(result.contains("address.city"));
        assertTrue(result.contains("New York"));
        verify(mockProcessor, times(1)).processJSON(input);
    }

    @Test
    void testCSVConverterDecoratorWithMissingFields() {
        // Arrange
        String input = "[{\"name\":\"John\",\"age\":30},{\"name\":\"Alice\"}]";
        when(mockProcessor.processJSON(input)).thenReturn(input);
        CSVConverterDecorator decorator = new CSVConverterDecorator(mockProcessor);

        // Act
        String result = decorator.processJSON(input);

        // Assert
        String[] lines = result.split("\n");
        assertEquals(3, lines.length); // Header + 2 data rows
        assertEquals(2, lines[0].split(",").length); // 2 columns
        verify(mockProcessor, times(1)).processJSON(input);
    }

    @Test
    void testCSVConverterDecoratorEmptyArray() {
        // Arrange
        String input = "[]";
        when(mockProcessor.processJSON(input)).thenReturn(input);
        CSVConverterDecorator decorator = new CSVConverterDecorator(mockProcessor);

        // Act
        String result = decorator.processJSON(input);

        // Assert
        assertEquals("", result);
        verify(mockProcessor, times(1)).processJSON(input);
    }

    @Test
    void testCSVConverterDecoratorInvalidInput() {
        // Arrange
        String input = "{\"name\":\"John\"}"; // Not an array
        when(mockProcessor.processJSON(input)).thenReturn(input);
        CSVConverterDecorator decorator = new CSVConverterDecorator(mockProcessor);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            decorator.processJSON(input);
        });
        assertTrue(exception.getMessage().contains("must be a JSON array"));
        verify(mockProcessor, times(1)).processJSON(input);
    }

    @Test
    void testCSVConverterDecoratorArrayWithNonObjects() {
        // Arrange
        String input = "[\"string\", 123]"; // Array with non-objects
        when(mockProcessor.processJSON(input)).thenReturn(input);
        CSVConverterDecorator decorator = new CSVConverterDecorator(mockProcessor);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            decorator.processJSON(input);
        });
        assertTrue(exception.getMessage().contains("must be a JSON object"));
        verify(mockProcessor, times(1)).processJSON(input);
    }

    @Test
    void testBasicJSONProcessorPassthrough() {
        // Arrange
        String input = "{\"name\":\"John\"}";
        BasicJSONProcessor processor = new BasicJSONProcessor();

        // Act
        String result = processor.processJSON(input);

        // Assert
        assertEquals(input, result);
    }
}