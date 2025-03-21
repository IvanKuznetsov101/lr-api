package com.vsu.events_spring.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${events_spring.app.lifetime}")
    private int lifetime;
    @Value("${events_spring.app.radius}")
    private int radius;
}