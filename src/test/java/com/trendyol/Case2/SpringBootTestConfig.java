package com.trendyol.Case2;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@PropertySource(value = {"classpath:product.properties"})
@PropertySource(value = {"classpath:cart.properties"})
@PropertySource(value = {"classpath:category.properties"})
@PropertySource(value = {"classpath:ValidationMessages.properties"})
@Slf4j
@ContextConfiguration(classes = {CucumberConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public abstract class SpringBootTestConfig {
}
