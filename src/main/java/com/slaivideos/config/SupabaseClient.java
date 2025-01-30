package com.slaivideos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SupabaseClient {

    @Value("${supabase.url}")  // 🔄 Variável de ambiente restaurada
    private String supabaseUrl;

    @Value("${supabase.key}")  // 🔄 Variável de ambiente restaurada
    private String supabaseKey;

    private final RestTemplate restTemplate;

    public SupabaseClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String buscarUsuarios() {
        String url = supabaseUrl + "/rest/v1/usuarios";  // ✅ URL agora vem do application.properties

        HttpHeaders headers = new HttpHeaders();
        headers.set("apikey", supabaseKey);
        headers.set("Authorization", "Bearer " + supabaseKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            return "Erro ao buscar usuários: " + response.getStatusCode();
        }
    }
}
