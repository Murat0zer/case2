package com.trendyol.Case2;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@ComponentScan(basePackages = "com.trendyol.Case2")
@ActiveProfiles("test")
public class CucumberConfiguration {
}
