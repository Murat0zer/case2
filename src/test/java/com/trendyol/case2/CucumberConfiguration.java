package com.trendyol.case2;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@ComponentScan(basePackages = "com.trendyol.case2")
@ActiveProfiles("test")
public class CucumberConfiguration {
}
