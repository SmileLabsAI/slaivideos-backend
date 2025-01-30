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

    public String salvarMensagem(ContactRequest requestData) throws IOException {
        String jsonBody = "{" +
                "\"nome\": \"" + requestData.getNome() + "\"," +
                "\"email\": \"" + requestData.getEmail() + "\"," +
                "\"mensagem\": \"" + requestData.getMensagem() + "\"" +
                "}";

        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json"));
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/mensagens")  // 🔹 Nome da tabela no Supabase: "mensagens"
                .post(body)
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Erro ao salvar a mensagem: " + response);
            }
            return "Mensagem salva com sucesso!";
        }
    }
}
