package com.slaivideos.config;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SupabaseConfig {

    private static final String SUPABASE_URL = "https://rxqieqpxjztnelrsibqc.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJ4cWllcXB4anp0bmVscnNpYnFjIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczNzgzMDMwNiwiZXhwIjoyMDUzNDA2MzA2fQ.gPPcCH_bLe3O3ncWfHd_W8SyAnxWLuts91wDTbmJETA";

    // Criando o Bean OkHttpClient para ser injetado onde necessário
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    // Método para testar a conexão com o Supabase
    public String testConnection() {
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/")
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .build();

        try (Response response = okHttpClient().newCall(request).execute()) {
            return response.isSuccessful() ? "Conexão bem-sucedida!" : "Falha na conexão!";
        } catch (IOException e) {
            return "Erro ao conectar: " + e.getMessage();
        }
    }
}
