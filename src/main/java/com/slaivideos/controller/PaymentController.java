package com.slaivideos.controller;

import com.slaivideos.model.PaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processPayment(@RequestBody PaymentRequest paymentRequest) {
        if (paymentRequest == null || paymentRequest.getItems() == null || paymentRequest.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Carrinho vazio!"));
        }

        // Calcular o valor total
        double totalAmount = paymentRequest.getItems().stream()
                .mapToDouble(item -> item.getUnit_price() * item.getQuantity())
                .sum();

        // Criar preferência de pagamento com totalAmount
        Map<String, String> paymentResponse = createPaymentPreference(paymentRequest.getItems(), totalAmount);

        if (paymentResponse == null) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Erro ao processar pagamento."));
        }

        return ResponseEntity.ok(paymentResponse);
    }

    private Map<String, String> createPaymentPreference(List<PaymentRequest.Item> items, double totalAmount) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.mercadopago.com/checkout/preferences";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("items", items.stream()
                .map(item -> Map.of(
                        "title", item.getTitle(),
                        "quantity", item.getQuantity(),
                        "currency_id", "BRL",
                        "unit_price", item.getUnit_price()
                ))
                .toList());
        body.put("total_amount", totalAmount);  // Adicionado o uso do totalAmount
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

            if (responseBody == null || !responseBody.containsKey("init_point")) {
                return null;
            }

            String initPoint = (String) responseBody.get("init_point");

            return Map.of(
                    "message", "Pagamento processado com sucesso!",
                    "init_point", initPoint
            );

        } catch (Exception e) {
            System.err.println("Erro ao comunicar com Mercado Pago: " + e.getMessage());
            return null;
        }
    }
}
