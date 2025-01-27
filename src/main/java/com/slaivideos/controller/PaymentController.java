package com.slaivideos.controller;

import com.slaivideos.model.PaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${mercadopago.public-key}")
    private String publicKey;

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @GetMapping("/public-key")
    public ResponseEntity<Map<String, String>> getPublicKey() {
        Map<String, String> response = new HashMap<>();
        response.put("publicKey", publicKey);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processPayment(@RequestBody PaymentRequest paymentRequest) {
        if (Objects.isNull(paymentRequest) ||
                paymentRequest.getAmount() <= 0 ||
                paymentRequest.getTitle() == null ||
                paymentRequest.getTitle().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Dados do pagamento inválidos."));
        }

        // Criar preferência de pagamento no Mercado Pago
        Map<String, String> paymentResponse = createPaymentPreference(paymentRequest);

        if (paymentResponse == null) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erro ao criar a preferência de pagamento."));
        }

        return ResponseEntity.ok(paymentResponse);
    }

    private Map<String, String> createPaymentPreference(PaymentRequest paymentRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.mercadopago.com/checkout/preferences";

        // Configuração dos headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Criando o corpo da requisição
        Map<String, Object> body = new HashMap<>();
        body.put("items", new Object[]{
                Map.of(
                        "title", paymentRequest.getTitle(),
                        "quantity", 1,
                        "currency_id", "BRL",
                        "unit_price", paymentRequest.getAmount()
                )
        });
        body.put("back_urls", Map.of(
                "success", "https://smilelabsai.github.io/SLAIVideos/sucesso.html",
                "failure", "https://smilelabsai.github.io/SLAIVideos/falha.html",
                "pending", "https://smilelabsai.github.io/SLAIVideos/pendente.html"
        ));
        body.put("auto_return", "approved");

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});
            Map<String, Object> responseBody = responseEntity.getBody();

            if (responseBody == null || !responseBody.containsKey("id") || !responseBody.containsKey("init_point")) {
                System.err.println("Erro: A resposta do Mercado Pago não contém os dados esperados!");
                return null;
            }

            String preferenceId = (String) responseBody.get("id");
            String initPoint = (String) responseBody.get("init_point");

            System.out.println("Preference ID recebido: " + preferenceId);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Pagamento processado com sucesso!");
            response.put("amount", String.valueOf(paymentRequest.getAmount()));
            response.put("id", preferenceId);
            response.put("init_point", initPoint);

            return response;
        } catch (Exception e) {
            System.err.println("Erro ao comunicar com Mercado Pago: " + e.getMessage());
            return null;
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> checkPaymentStatus() {
        return ResponseEntity.ok(Map.of("status", "Aprovado"));
    }
}
