package com.slaivideos.service;

import okhttp3.*;
import org.springframework.stereotype.Service;
import com.slaivideos.model.ContactRequest;
import java.io.IOException;

@Service
public class ContactService {
    private final OkHttpClient client = new OkHttpClient();

    private static final String SUPABASE_URL = "https://rxqieqpxjztnelrsibqc.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJ4cWllcXB4anp0bmVscnNpYnFjIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczNzgzMDMwNiwiZXhwIjoyMDUzNDA2MzA2fQ.gPPcCH_bLe3O3ncW8SyAnxWLuts91wDTbmJETA";

    private static final String TABLE_NAME = "Contato";

    public String salvarMensagem(ContactRequest requestData) {
        String jsonBody = String.format(
                "{\"nome\": \"%s\", \"email\": \"%s\", \"mensagem\": \"%s\"}",
                escapeJson(requestData.getNome()),
                escapeJson(requestData.getEmail()),
                escapeJson(requestData.getMensagem())
        );

        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/" + TABLE_NAME)
                .post(body)
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorResponse = response.body() != null ? response.body().string() : "Erro desconhecido";
                throw new IOException("Erro ao salvar a mensagem: " + errorResponse);
            }
            return "Mensagem salva com sucesso!";
        } catch (IOException e) {
            return "Falha ao conectar ao banco de dados: " + e.getMessage();
        }
    }

    private String escapeJson(String value) {
        return value != null ? value.replace("\"", "\\\"").replace("\n", "\\n") : "";
    }
}
