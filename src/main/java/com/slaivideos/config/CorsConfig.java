package com.slaivideos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "https://smilelabsai.github.io",
                        "https://slaivideos-backend-1.onrender.com",
                        "https://rxqieqpxjztnelrsibqc.supabase.com"
                )
                .allowedMethods("*") // ✅ Agora aceita TODOS os métodos, incluindo OPTIONS
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
