package pl.put.poznan.processor.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CSVConverterDecoratorOrderTest {

    @Test
    void testColumnOrderIsPreserved() {
        // Arrange
        String input = "[{\"name\":\"John\",\"age\":30,\"address\":{\"city\":\"New York\",\"zip\":10001}}]";
        CSVConverterDecorator decorator = new CSVConverterDecorator(new BasicJSONProcessor());

        // Act
        String result = decorator.processJSON(input);

        // Assert
        String[] lines = result.split("\n");
        String[] headers = lines[0].split(",");

        // Check that name comes before age in the headers
        int nameIndex = -1;
        int ageIndex = -1;

        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals("name")) nameIndex = i;
            if (headers[i].equals("age")) ageIndex = i;
        }

        assertTrue(nameIndex < ageIndex, "Name should come before age in the CSV headers");

        // Check that address fields follow the original order
        int cityIndex = -1;
        int zipIndex = -1;

        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals("address.city")) cityIndex = i;
            if (headers[i].equals("address.zip")) zipIndex = i;
        }

        assertTrue(cityIndex < zipIndex, "City should come before zip in the CSV headers");
    }

    @Test
    void testColumnOrderWithMultipleObjects() {
        // Arrange
        String input = "[{\"name\":\"John\",\"age\":30},{\"address\":{\"city\":\"New York\"},\"name\":\"Alice\"}]";
        CSVConverterDecorator decorator = new CSVConverterDecorator(new BasicJSONProcessor());

        // Act
        String result = decorator.processJSON(input);

        // Assert
        String[] lines = result.split("\n");
        String[] headers = lines[0].split(",");

        // Check that fields from the first object appear before fields from the second
        int nameIndex = -1;
        int ageIndex = -1;
        int addressCityIndex = -1;

        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals("name")) nameIndex = i;
            if (headers[i].equals("age")) ageIndex = i;
            if (headers[i].equals("address.city")) addressCityIndex = i;
        }

        assertTrue(nameIndex < addressCityIndex, "Name should appear before address.city in headers");
        assertTrue(ageIndex < addressCityIndex, "Age should appear before address.city in headers");
    }

    @Test
    void testCSVOutput() {
        // Arrange
        String input = "[{\"name\":\"John\",\"age\":30,\"address\":{\"city\":\"New York\",\"zip\":10001}}," +
                "{\"name\":\"Alice\",\"age\":25,\"address\":{\"city\":\"Boston\",\"street\":\"Main St\"}}]";
        CSVConverterDecorator decorator = new CSVConverterDecorator(new BasicJSONProcessor());

        // Act
        String result = decorator.processJSON(input);

        // Assert
        // Check the CSV structure
        String[] lines = result.split("\n");
        assertEquals(3, lines.length); // Header + 2 data rows

        // Ensure data appears in the expected rows
        assertTrue(lines[1].contains("John") && lines[1].contains("30") &&
                lines[1].contains("New York") && lines[1].contains("10001"));
        assertTrue(lines[2].contains("Alice") && lines[2].contains("25") &&
                lines[2].contains("Boston") && lines[2].contains("Main St"));
    }
}