package pl.put.poznan.processor.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"pl.put.poznan.processor.rest"})
class JSONToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JSONToolsApplication.class, args);
    }
}
