package pl.put.poznan.processor.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main application class for the JSON Tools application.
 * Configures and starts the Spring Boot application.
 */
@SpringBootApplication(scanBasePackages = {"pl.put.poznan.processor.rest"})
class JSONToolsApplication {

    /**
     * The main method to start the application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(JSONToolsApplication.class, args);
    }
}
