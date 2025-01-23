package com.slaivideos.controller;  // Verifique se esse caminho está correto

import com.slaivideos.model.PaymentRequest;  // Importando a classe PaymentRequest
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @PostMapping("/payment")
    public String processPayment(@RequestBody PaymentRequest paymentRequest) {
        return "Pagamento processado com sucesso! Valor: " + paymentRequest.getAmount();
    }
}
