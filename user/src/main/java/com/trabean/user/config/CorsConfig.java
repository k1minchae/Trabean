package com.trabean.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;


@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("http://localhost:3000");
        config.addAllowedOriginPattern("http://localhost:8080/api/user/login");
        config.addAllowedOriginPattern("https://j11a604.p.ssafy.io/api/user/login");
        config.addAllowedOriginPattern("https://j11a604.p.ssafy.io/login");
        config.addAllowedOriginPattern("http://j11a604.p.ssafy.io/login");
        config.addAllowedOriginPattern("http://j11a604.p.ssafy.io:8888/login");
        config.addAllowedOriginPattern("https://j11a604.p.ssafy.io:8888/login");
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control","Content-Type","raw","*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        config.addExposedHeader("Authorization");
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
