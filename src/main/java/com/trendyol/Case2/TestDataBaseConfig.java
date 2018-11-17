package com.trendyol.Case2;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.data.map.repository.config.MapRepositoryConfigurationExtension;

@Configuration
@EnableMapRepositories(basePackages = {"com.trendyol.Case2"})
@Profile("test")
public class TestDataBaseConfig extends MapRepositoryConfigurationExtension {

}
