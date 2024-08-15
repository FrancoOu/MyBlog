package org.franco.config;

import org.checkerframework.checker.units.qual.C;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.franco.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Franco", "https://francoou.com", "franco52048@gmail.com");

        return new ApiInfoBuilder()
                .title("Blog Doc")
                .description("This is API doc for the blog system")
                .contact(contact)
                .version("0.0.1")
                .build();
    }
}
