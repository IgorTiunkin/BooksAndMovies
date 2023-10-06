package com.phantom.booksandmovies;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableCaching
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
