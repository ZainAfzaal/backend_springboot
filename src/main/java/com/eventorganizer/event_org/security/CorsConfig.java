package com.eventorganizer.event_org.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(false); // Keep false since you're using JWT in Authorization header (not cookies)
        config.addAllowedOrigin("*");      // Change to your frontend URL in production, e.g. "https://your-frontend.com"
        config.addAllowedHeader("*");      // Allows Content-Type, Authorization, etc.
        config.addAllowedMethod("*");      // Allows GET, POST, PUT, DELETE, OPTIONS, etc.

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}