package com.slaivideos.controller;

import com.slaivideos.model.PaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${mercadopago.public-key}")
    private String publicKey;

    @GetMapping("/public-key")
    public ResponseEntity<Map<String, String>> getPublicKey() {
        Map<String, String> response = new HashMap<>();
        response.put("publicKey", publicKey);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processPayment(@RequestBody PaymentRequest paymentRequest) {
        if (Objects.isNull(paymentRequest) || paymentRequest.getAmount() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Valor do pagamento inválido."));
        }

        // Simula um ID único (normalmente, o ID viria do Mercado Pago)
        String fakeId = "mp-" + System.currentTimeMillis();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Pagamento processado com sucesso!");
        response.put("amount", String.valueOf(paymentRequest.getAmount()));
        response.put("id", fakeId);  // ✅ Agora retorna um "id"
        response.put("init_point", "https://www.mercadopago.com.br/checkout/v1/redirect?pref_id=" + fakeId); // ✅ Correção aqui

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> checkPaymentStatus() {
        return ResponseEntity.ok(Map.of("status", "Aprovado"));
    }
}
