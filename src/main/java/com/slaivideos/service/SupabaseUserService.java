package com.slaivideos.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import okhttp3.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SupabaseUserService {

    private final OkHttpClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    private final String JWT_SECRET = "chaveSecreta"; // 🔐 Definir uma chave segura para o JWT
    private final long EXPIRATION_TIME = 3600000; // 1 hora em milissegundos

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
            String queryUrl = supabaseUrl + "/rest/v1/usuarios?email=eq." + email + "&select=email,senha";
            Request request = new Request.Builder()
                    .url(queryUrl)
                    .addHeader("apikey", supabaseKey)
                    .addHeader("Authorization", "Bearer " + supabaseKey)
                    .addHeader("Accept", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return "Erro ao buscar usuário: " + response.message();
                }

                String responseBody = response.body() != null ? response.body().string() : "";
                if (responseBody.isEmpty() || responseBody.equals("[]")) {
                    return "Usuário não encontrado.";
                }

                // 🔍 Validando resposta JSON do Supabase
                JsonNode rootNode = objectMapper.readTree(responseBody);
                if (!rootNode.isArray() || rootNode.size() == 0) {
                    return "Usuário não encontrado.";
                }

                // 🔑 Pegando a senha criptografada do primeiro usuário encontrado
                String senhaCriptografada = rootNode.get(0).get("senha").asText();

                // 🔒 Comparação de senha segura
                if (BCrypt.checkpw(senha, senhaCriptografada)) {
                    // 🔑 Gerar um token JWT para autenticação
                    String jwtToken = Jwts.builder()
                            .setSubject(email)
                            .setIssuedAt(new Date())
                            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 1h de expiração
                            .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                            .compact();

                    return "{\"token\": \"" + jwtToken + "\", \"message\": \"Login bem-sucedido!\"}";
                } else {
                    return "Senha incorreta.";
                }
            }
        } catch (IOException e) {
            return "Erro de conexão: " + e.getMessage();
        }
    }
}
