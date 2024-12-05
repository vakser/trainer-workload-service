package com.epam.learn.trainerworkloadservice.configuration;

import com.epam.learn.trainerworkloadservice.jwt.JwtRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<JwtRequestFilter> jwtValidationFilter(JwtRequestFilter jwtRequestFilter) {
        FilterRegistrationBean<JwtRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtRequestFilter);
        registrationBean.addUrlPatterns("/api/workload/*"); // Apply to specific paths if necessary
        registrationBean.setOrder(1); // Order of execution (lower means earlier)
        return registrationBean;
    }
}
