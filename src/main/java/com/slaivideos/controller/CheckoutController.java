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

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Value("${mercadopago.access-token}")
    private String mercadoPagoAccessToken;

    @PostMapping("/create_preference")
    public ResponseEntity<?> createPreference(@RequestBody CheckoutRequest checkoutRequest) {
        try {
            // Configura o Mercado Pago
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

            // Verifica se os dados são válidos
            if (checkoutRequest.getTitle() == null || checkoutRequest.getPrice() <= 0) {
                return ResponseEntity.badRequest().body("Erro: Dados inválidos para criar a preferência.");
            }

            // Cria o item de pagamento
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title(checkoutRequest.getTitle())
                    .quantity(1)
                    .unitPrice(BigDecimal.valueOf(checkoutRequest.getPrice()))
                    .currencyId("BRL")
                    .build();

            // Cria a preferência de pagamento
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(Collections.singletonList(itemRequest))
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            // Retorna o ID da preferência
            return ResponseEntity.ok(preference.getId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao criar preferência: " + e.getMessage());
        }
    }
}
