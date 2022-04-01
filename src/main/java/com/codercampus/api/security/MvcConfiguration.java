package com.codercampus.api.security;

import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

// sets SameSite cookie attribute to NONE
@Configuration
@EnableWebMvc
public class MvcConfiguration implements WebMvcConfigurer {
    @Bean
    public TomcatContextCustomizer sameSiteCookiesConfig() {
        return context -> {
            final Rfc6265CookieProcessor cookieProcessor = new Rfc6265CookieProcessor();
            cookieProcessor.setSameSiteCookies(SameSiteCookies.NONE.getValue());
            context.setCookieProcessor(cookieProcessor);
        };
    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")
//                .allowedOrigins("https://127.0.0.1:3000","https://expense-tracker-client-2022.herokuapp.com/","https://localhost:3000")
//                .allowCredentials(true)
//                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
//                .allowedHeaders("Set-Cookie","Authorization", "Content-Type","Accept")
//                .maxAge(3600);
//    }
}
