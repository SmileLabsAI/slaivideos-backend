package com.slaivideos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permite todas as rotas
                        .allowedOrigins(
                                "https://smilelabsai.github.io",
                                "https://slaivideos-backend.onrender.com"
                        ) // Permitir requisições do frontend e do próprio backend
                        .allowedMethods("*") // Permitir todos os métodos HTTP
                        .allowedHeaders("*") // Permitir todos os headers
                        .allowCredentials(true) // Permitir credenciais (se necessário)
                        .maxAge(3600); // Cache de CORS por 1 hora
            }
        };
    }
}
