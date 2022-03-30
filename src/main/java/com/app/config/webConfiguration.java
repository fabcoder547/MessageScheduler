package com.app.config;

import com.app.interceptors.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;





@Component
public class webConfiguration implements WebMvcConfigurer {

	@Autowired
	AuthInterceptor authInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		System.out.println("In configuration");
		registry.addInterceptor(authInterceptor).addPathPatterns("/schedule/message");
	}
	
	
}





































