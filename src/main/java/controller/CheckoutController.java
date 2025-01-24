package com.slaivideos.controller;

import com.slaivideos.model.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PostMapping("/checkout")
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            // Verifica se o request é válido
            if (paymentRequest == null || paymentRequest.getAmount() == null || paymentRequest.getAmount() <= 0.0) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Valor do pagamento inválido.");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Simula um pagamento processado com sucesso
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Pagamento processado com sucesso!");
            response.put("amount", paymentRequest.getAmount());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro ao processar pagamento: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> checkPaymentStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Aprovado");
        return ResponseEntity.ok(response);
    }
}
