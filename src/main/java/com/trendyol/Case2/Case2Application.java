package com.trendyol.Case2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.IOException;
import java.util.Properties;

//@SpringBootApplication
@EnableAutoConfiguration(
        exclude = {MongoAutoConfiguration.class,
                MongoDataAutoConfiguration.class,
                MongoRepositoriesAutoConfiguration.class})

@EnableConfigurationProperties
@ComponentScan
@Configuration
public class Case2Application {

    @Bean
    public Properties productProperties() throws IOException {
        return PropertiesLoaderUtils.loadAllProperties("product.properties");
    }

    @Bean
    public Properties categoryProperties() throws IOException {
        return PropertiesLoaderUtils.loadAllProperties("category.properties");
    }

    @Bean
    public Properties cartProperties() throws IOException {
        return PropertiesLoaderUtils.loadAllProperties("cart.properties");
    }

    @Bean
    public Properties campaignProperties() throws IOException {
        return PropertiesLoaderUtils.loadAllProperties("campaign.properties");
    }

	public static void main(String[] args) {
		SpringApplication.run(Case2Application.class, args);
	}


}
