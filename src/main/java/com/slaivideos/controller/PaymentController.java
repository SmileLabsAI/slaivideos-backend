package com.slaivideos.controller;

import com.slaivideos.model.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, String>> processPayment(@RequestBody PaymentRequest paymentRequest) {
        if (Objects.isNull(paymentRequest) || paymentRequest.getAmount() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Valor do pagamento inválido."));
        }

        // Simula um pagamento processado com sucesso
        Map<String, String> response = new HashMap<>();
        response.put("message", "Pagamento processado com sucesso!");
        response.put("amount", String.valueOf(paymentRequest.getAmount()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> checkPaymentStatus() {
        return ResponseEntity.ok(Map.of("status", "Aprovado"));
    }
}
