package com.slaivideos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import okhttp3.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupabaseUserService {

    private final OkHttpClient client;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Manipulação de JSON

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
                .addHeader("Content-Type", "application/json") // ✅ Corrigido
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

    public String criarUsuario(String nome, String eMail, String senha) { // ✅ "eMail" corrigido
        try {
            // Criptografando senha antes de salvar no banco
            String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt());

            Map<String, String> userData = new HashMap<>();
            userData.put("nome", nome);
            userData.put("e-mail", eMail); // ✅ "e-mail" corrigido
            userData.put("senha", senhaCriptografada);

            String jsonBody = objectMapper.writeValueAsString(userData);

            RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(supabaseUrl + "/rest/v1/usuarios")
                    .post(body)
                    .addHeader("apikey", supabaseKey)
                    .addHeader("Authorization", "Bearer " + supabaseKey)
                    .addHeader("Content-Type", "application/json") // ✅ Corrigido
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

    public String loginUsuario(String eMail, String senha) { // ✅ "eMail" corrigido
        try {
            // Buscar usuário pelo e-mail no Supabase
            Request request = new Request.Builder()
                    .url(supabaseUrl + "/rest/v1/usuarios?e-mail=eq." + eMail + "&select=e-mail,senha") // ✅ Corrigido
                    .addHeader("apikey", supabaseKey)
                    .addHeader("Authorization", "Bearer " + supabaseKey)
                    .addHeader("Content-Type", "application/json") // ✅ Corrigido
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    return "Erro ao buscar usuário: " + response.message();
                }

                String responseBody = response.body() != null ? response.body().string() : "";
                if (responseBody.isEmpty() || responseBody.equals("[]")) {
                    return "Usuário não encontrado.";
                }

                // Utilizar ObjectMapper para fazer o parsing do JSON
                var resultList = objectMapper.readValue(responseBody,
                        new TypeReference<List<Map<String, Object>>>() {});
                if (resultList.isEmpty()) {
                    return "Usuário não encontrado.";
                }

                // Extrai a senha criptografada do primeiro registro (supondo e-mail único)
                String senhaCriptografada = (String) resultList.get(0).get("senha");

                // Verifica a senha informada com a senha armazenada
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
