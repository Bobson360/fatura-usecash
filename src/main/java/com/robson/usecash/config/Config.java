package com.robson.usecash.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackages = {"com.robson.usecash.service"})
public class Config {
	 @Bean
	    public WebMvcConfigurer corsConfigurer() {
	        return new WebMvcConfigurer() {
	            @Override
	            public void addCorsMappings(CorsRegistry registry) {
	                registry.addMapping("/file/upload")
	                        .allowedOrigins("https://fatura-usecash-front.vercel.app")
	                        .allowedMethods("POST")
	                        .allowCredentials(true)
	                        .maxAge(3600);
	            }
	        };
	    }
}
