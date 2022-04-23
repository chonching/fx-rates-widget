package com.example.exam.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="exchange.rates")
public class CurrencyConverterConfig {

    private String apiKey;
    private String apiUrl;
}
