package com.slaivideos.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slaivideos.dto.LoginResponseDTO;
import com.slaivideos.dto.UserRequestDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import okhttp3.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class SupabaseUserService {

    private final OkHttpClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final long EXPIRATION_TIME = 3600000; // 1 hora

    public SupabaseUserService(OkHttpClient client) {
        this.client = client;
    }

    // ✅ Método para LOGIN
    public ResponseEntity<?> loginUsuario(String email, String senha) {
        try {
            String queryUrl = supabaseUrl + "/rest/v1/usuarios?email=eq." + email + "&select=email,senha";
            Request request = new Request.Builder()
                    .url(queryUrl)
                    .addHeader("apikey", supabaseKey)
                    .addHeader("Authorization", "Bearer " + supabaseKey)
                    .addHeader("Accept", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return ResponseEntity.status(response.code()).body(Map.of("error", "Erro ao buscar usuário: " + response.message()));
                }

                String responseBody = response.body().string();
                if (responseBody.isEmpty() || responseBody.equals("[]")) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuário não encontrado."));
                }

                JsonNode rootNode = objectMapper.readTree(responseBody);
                String senhaCriptografada = rootNode.get(0).get("senha").asText();

                if (BCrypt.checkpw(senha, senhaCriptografada)) {
                    Key signingKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));

                    String jwtToken = Jwts.builder()
                            .setSubject(email)
                            .setIssuedAt(new Date())
                            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                            .signWith(signingKey, SignatureAlgorithm.HS256)
                            .compact();

                    return ResponseEntity.ok(new LoginResponseDTO(jwtToken, "Login bem-sucedido!"));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Senha incorreta."));
                }
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Erro de conexão: " + e.getMessage()));
        }
    }

    // ✅ Método para CADASTRO corrigido
    public ResponseEntity<?> criarUsuario(UserRequestDTO request) {
        try {
            String url = supabaseUrl + "/rest/v1/usuarios";
            String senhaCriptografada = BCrypt.hashpw(request.getSenha(), BCrypt.gensalt());

            // Incluindo nome no JSON
            String jsonBody = String.format(
                    "{\"nome\": \"%s\", \"email\": \"%s\", \"senha\": \"%s\"}",
                    request.getNome(), request.getEmail(), senhaCriptografada
            );

            RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));
            Request requestHttp = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("apikey", supabaseKey)
                    .addHeader("Authorization", "Bearer " + supabaseKey)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=minimal")
                    .build();

            try (Response response = client.newCall(requestHttp).execute()) {
                if (!response.isSuccessful()) {
                    return ResponseEntity
                            .status(response.code())
                            .body(Map.of("error", "Erro ao cadastrar usuário: " + response.message()));
                }
                return ResponseEntity.ok(Map.of("message", "Usuário cadastrado com sucesso!"));
            }
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao conectar ao Supabase: " + e.getMessage()));
        }
    }
}
