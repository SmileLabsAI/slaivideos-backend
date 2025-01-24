package com.slaivideos.controller;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.slaivideos.model.CheckoutRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Value("${mercadopago.access-token}")
    private String mercadoPagoAccessToken;

    @PostMapping("/create_preference")
    public ResponseEntity<?> createPreference(@RequestBody CheckoutRequest checkoutRequest) {
        try {
            if (checkoutRequest == null || checkoutRequest.getTitle() == null || checkoutRequest.getTitle().isEmpty() ||
                    checkoutRequest.getPrice() == null || checkoutRequest.getPrice() <= 0) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Dados inválidos para criar a preferência.");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title(checkoutRequest.getTitle())
                    .quantity(1)
                    .unitPrice(BigDecimal.valueOf(checkoutRequest.getPrice()))
                    .currencyId("BRL")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(Collections.singletonList(itemRequest))
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("id", preference.getId());
            successResponse.put("message", "Preferência criada com sucesso.");

            return ResponseEntity.ok(successResponse);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Erro ao criar a preferência de pagamento: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
