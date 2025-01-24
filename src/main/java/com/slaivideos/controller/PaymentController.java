package com.slaivideos.controller;

import com.slaivideos.model.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @PostMapping("/checkout")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {
        // Simula um pagamento processado com sucesso
        return ResponseEntity.ok("Pagamento processado com sucesso! Valor: " + paymentRequest.getAmount());
    }

    @GetMapping("/status")
    public ResponseEntity<String> checkPaymentStatus() {
        // Simula uma verificação de status do pagamento
        return ResponseEntity.ok("Status do pagamento: Aprovado");
    }
}
