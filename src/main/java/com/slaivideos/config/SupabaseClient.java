package com.slaivideos.config;

import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SupabaseClient {

    private static final String SUPABASE_URL = "https://rxqieqpxjztnelrsibqc.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJ4cWllcXB4anp0bmVscnNpYnFjIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mzc4MzAzMDYsImV4cCI6MjA1MzQwNjMwNn0.-eFyRvUhRRGwS5u2zOdKjhHronlw8u-POJzCaBocBxc";

    private final OkHttpClient client = new OkHttpClient();

    public String buscarUsuarios() throws IOException {
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/usuarios")
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .addHeader("Content-Type", "application/json")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body() != null ? response.body().string() : "Erro: resposta vazia";
        }
    }
}
