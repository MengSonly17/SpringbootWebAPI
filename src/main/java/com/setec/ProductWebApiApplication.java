package com.setec;

//import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductWebApiApplication {

    public static void main(String[] args) {
//        // Load environment variables from the .env file
//        Dotenv dotenv = Dotenv.load();
//
//        // Set environment variables as system properties
//        System.setProperty("spring.datasource.username", dotenv.get("DB_USERNAME"));
//        System.setProperty("spring.datasource.password", dotenv.get("DB_PASSWORD"));
//        System.setProperty("spring.datasource.url", dotenv.get("DB_URL"));
//        System.setProperty("spring.datasource.port", dotenv.get("APP_PORT"));
        
        // Run the Spring Boot application
        SpringApplication.run(ProductWebApiApplication.class, args);
    }
}
