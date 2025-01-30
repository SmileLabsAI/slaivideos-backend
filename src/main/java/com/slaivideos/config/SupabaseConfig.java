package com.slaivideos.config;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SupabaseConfig {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    // Método para testar a conexão com Supabase
    public String testConnection(OkHttpClient client) {  // ✅ Agora recebe o OkHttpClient como parâmetro
        Request request = new Request.Builder()
                .url(supabaseUrl + "/rest/v1/usuarios")
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer " + supabaseKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful() ? "Conexão com Supabase bem-sucedida! ✅"
                    : "Falha na conexão ❌ Código: " + response.code();
        } catch (IOException e) {
            return "Erro ao conectar: " + e.getMessage();
        }
    }
}
