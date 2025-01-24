package com.slaivideos.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @PostMapping("/create_preference")
    public ResponseEntity<?> createPreference() {
        try {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Dados inválidos para criar a preferência.");
            return ResponseEntity.badRequest().body(errorResponse);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro ao criar a preferência de pagamento: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

}
