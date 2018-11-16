package com.trendyol.Case2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

@Configuration
@SpringBootApplication
public class Case2Application {

    @Bean
    public Properties productProperties() throws IOException {
        return PropertiesLoaderUtils.loadAllProperties("product.properties");
    }

    @Bean
    public Properties categoryProperties() throws IOException {
        return PropertiesLoaderUtils.loadAllProperties("category.properties");
    }

	public static void main(String[] args) {
		SpringApplication.run(Case2Application.class, args);
	}


}
