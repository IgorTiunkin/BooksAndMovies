package com.phantom.booksandmovies.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerCustomConfiguration {

    @Bean
    public Docket swaggerConfiguration () {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.phantom"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo () {
        return new ApiInfo(
                "BooksAndMovies API",
                "API for tracking watched movies and read books",
                "1.0",
                "Free to use",
                new Contact("Phantom", "phantom.com", "phantom@phantom.com"),
                "API licence",
                "phantom.com",
                Collections.emptyList()
        );
    }
}
