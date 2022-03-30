package com.app.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Timer;

@Configuration
public class TimerConfig {

    @Bean
    public Timer getTimer() {
        return new Timer();
    }

}
