package com.app.config;

import com.app.interceptors.AuthInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
    This will configure interceptors for our application
    so that every request will go through interceptors for authentication
 */


@Component
public class webConfiguration implements WebMvcConfigurer {

    Logger logger = LoggerFactory.getLogger(webConfiguration.class);
    @Autowired
    AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("confuguring interceptors.......");
        //add authInterceptor for authentication..
        registry.addInterceptor(authInterceptor).addPathPatterns("/schedule/message");
    }


}





































