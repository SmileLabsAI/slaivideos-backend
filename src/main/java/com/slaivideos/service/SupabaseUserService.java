package com.slaivideos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SupabaseUserService {

    private final OkHttpClient client;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Para manipular JSON

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    public SupabaseUserService(OkHttpClient client) {
        this.client = client;
    }

    public String listarUsuarios() {
        Request request = new Request.Builder()
                .url(supabaseUrl + "/rest/v1/usuarios?select=*")
                .addHeader("apikey", supabaseKey)
                .addHeader("Authorization", "Bearer " + supabaseKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Erro ao buscar usuários: " + response.message();
            }
            return response.body() != null ? response.body().string() : "Nenhum usuário encontrado.";
        } catch (IOException e) {
            return "Erro de conexão: " + e.getMessage();
        }
    }

    public String criarUsuario(String nome, String email, String senha) {
        try {
            // 🔒 Criptografando senha antes de salvar no banco
            String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt());

            Map<String, String> userData = new HashMap<>();
            userData.put("nome", nome);
            userData.put("email", email);
            userData.put("senha", senhaCriptografada);

            String jsonBody = objectMapper.writeValueAsString(userData);

            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(supabaseUrl + "/rest/v1/usuarios")
                    .post(body)
                    .addHeader("apikey", supabaseKey)
                    .addHeader("Authorization", "Bearer " + supabaseKey)
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return "Erro ao criar usuário: " + response.message();
                }
                return "Usuário criado com sucesso!";
            }
        } catch (IOException e) {
            return "Erro ao processar a requisição: " + e.getMessage();
        }
    }

    public String loginUsuario(String email, String senha) {
        try {
            // 🔍 Buscar usuário pelo e-mail no Supabase
            Request request = new Request.Builder()
                    .url(supabaseUrl + "/rest/v1/usuarios?email=eq." + email + "&select=email,senha")
                    .addHeader("apikey", supabaseKey)
                    .addHeader("Authorization", "Bearer " + supabaseKey)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return "Erro ao buscar usuário: " + response.message();
                }

                String responseBody = response.body() != null ? response.body().string() : "";
                if (responseBody.isEmpty() || responseBody.equals("[]")) {
                    return "Usuário não encontrado.";
                }

                // 🔑 Extrair senha salva no Supabase (criptografada)
                String senhaCriptografada = responseBody.split("\"senha\":\"")[1].split("\"")[0];

                // 🔒 Verificar senha digitada com a senha salva (comparação segura)
                if (BCrypt.checkpw(senha, senhaCriptografada)) {
                    return "Login bem-sucedido!";
                } else {
                    return "Senha incorreta.";
                }
            }
        } catch (IOException e) {
            return "Erro de conexão: " + e.getMessage();
        }
    }
}
