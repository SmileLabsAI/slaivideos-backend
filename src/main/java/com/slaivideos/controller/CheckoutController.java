package com.slaivideos.controller;

import com.slaivideos.model.CheckoutRequest;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    // Access Token do Mercado Pago (⚠️ NÃO RECOMENDADO DEIXAR NO CÓDIGO)
    private static final String MERCADO_PAGO_ACCESS_TOKEN = "APP_USR-2558136151487812-012311-08dd4e15416784773f715f16a694e92f-220300097";

    @PostMapping("/create_preference")
    public ResponseEntity<?> createPreference(@RequestBody CheckoutRequest checkoutRequest) {
        try {
            // Configura o Mercado Pago com o Access Token
            MercadoPagoConfig.setAccessToken(MERCADO_PAGO_ACCESS_TOKEN);

            // Criação do item de pagamento
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title(checkoutRequest.getTitle())
                    .quantity(1)
                    .unitPrice(BigDecimal.valueOf(checkoutRequest.getPrice()))
                    .currencyId("BRL")
                    .build();

            // Criação da preferência de pagamento
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(Collections.singletonList(itemRequest))
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return ResponseEntity.ok(preference.getId());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao criar preferência: " + e.getMessage());
        }
    }
}
