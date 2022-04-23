package com.example.exam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.RequestHandlerSelectors;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;


@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig {

    @Bean
    public Docket notificationPlatformAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.exam.controller"))
                .build()
                .apiInfo(apiInfo());
    }

    public ApiInfo apiInfo(){
        return new ApiInfo(
                "FX Rates Widget",
                "This API converts one currency to another based on selection",
                "1.0",
                "Terms of Services...",
                new Contact("Marlon Antonio Taguicana", "", "marlontaguicana@gmail.com"),
                "License Version 1.0", "", Collections.emptyList()
        );
    }
}
