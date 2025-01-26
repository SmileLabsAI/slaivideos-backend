package com.slaivideos.service;

import org.springframework.stereotype.Service;
import okhttp3.*;
import java.io.IOException;

@Service  // Garante que o Spring reconheça esta classe como um Bean
public class SupabaseUserService {

    private final OkHttpClient client = new OkHttpClient();
    private static final String SUPABASE_URL = "https://rxqieqpxjztnelrsibqc.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJ4cWllcXB4anp0bmVscnNpYnFjIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczNzgzMDMwNiwiZXhwIjoyMDUzNDA2MzA2fQ.gPPcCH_bLe3O3ncWfHd_W8SyAnxWLuts91wDTbmJETA";

    public String listarUsuarios() throws IOException {
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/usuarios?select=*")
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro na requisição: " + response);
            }
            return response.body() != null ? response.body().string() : "Nenhum dado encontrado";
        }
    }

    public String criarUsuario(String nome, String email, String senha) throws IOException {
        String jsonBody = "{" +
                "\"nome\": \"" + nome + "\"," +
                "\"email\": \"" + email + "\"," +
                "\"senha\": \"" + senha + "\"}";

        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/usuarios")
                .post(body)
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao criar usuário: " + response);
            }
            return "Usuário criado com sucesso!";
        }
    }
}
