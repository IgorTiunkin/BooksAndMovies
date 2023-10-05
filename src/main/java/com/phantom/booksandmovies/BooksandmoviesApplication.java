package com.phantom.booksandmovies;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class BooksandmoviesApplication {

    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.load();
        } catch (DotenvException dotenvException) {
            System.out.println("Environment file not found");
        }
        SpringApplication.run(BooksandmoviesApplication.class, args);
    }


}
