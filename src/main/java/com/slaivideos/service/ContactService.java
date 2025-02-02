package com.slaivideos.service;

import okhttp3.*;
import org.springframework.stereotype.Service;
import com.slaivideos.model.ContactRequest;
import java.io.IOException;

@Service
public class ContactService {
    private final OkHttpClient client = new OkHttpClient();

    private static final String SUPABASE_URL = "https://rxqieqpxjztnelrsibqc.supabase.co/rest/v1";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJ4cWllcXB4anp0bmVscnNpYnFjIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczNzgzMDMwNiwiZXhwIjoyMDUzNDA2MzA2fQ.gPPcCH_bLe3O3ncWfHd_W8SyAnxWLuts91wDTbmJETA";

    private static final String TABLE_NAME = "Contato";  // Nome exato da tabela no Supabase

    public String salvarMensagem(ContactRequest requestData) {
        try {
            // Construindo o JSON corretamente
            String jsonBody = String.format(
                    "{\"nome\": \"%s\", \"email\": \"%s\", \"mensagem\": \"%s\"}",
                    escapeJson(requestData.getNome()),
                    escapeJson(requestData.getEmail()),
                    escapeJson(requestData.getMensagem())
            );

            RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(SUPABASE_URL + "/" + TABLE_NAME)  // Mantendo a estrutura anterior
                    .post(body)
                    .addHeader("apikey", SUPABASE_KEY)
                    .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=minimal")  // 🔥 Adicionando o cabeçalho correto
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    // Obtendo resposta de erro se houver
                    String errorResponse = response.body() != null ? response.body().string() : "Erro desconhecido";
                    return "Erro ao salvar a mensagem: " + errorResponse;
                }
                return "Mensagem salva com sucesso!";
            }
        } catch (IOException e) {
            return "Falha ao conectar ao banco de dados: " + e.getMessage();
        }
    }

    // Método para evitar problemas com caracteres especiais no JSON
    private String escapeJson(String value) {
        return value != null ? value.replace("\"", "\\\"").replace("\n", "\\n") : "";
    }
}
