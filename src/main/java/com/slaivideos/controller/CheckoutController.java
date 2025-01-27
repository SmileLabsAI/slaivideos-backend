package com.slaivideos.controller;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.*;
import com.mercadopago.resources.preference.Preference;
import com.slaivideos.model.CheckoutRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class CheckoutController {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostMapping("/checkout")
    public ResponseEntity<?> createPreference(@RequestBody CheckoutRequest request) {
        try {
            // Configurar Mercado Pago
            MercadoPagoConfig.setAccessToken(accessToken);

            // Criar lista de itens
            List<PreferenceItemRequest> items = new ArrayList<>();
            items.add(PreferenceItemRequest.builder()
                    .title(request.getTitle())
                    .quantity(1)
                    .currencyId("BRL")
                    .unitPrice(BigDecimal.valueOf(request.getPrice()))
                    .build());

            // Criar preferência de pagamento
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .build();

            // Criar a preferência no Mercado Pago
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            // Retornar o ID da preferência para o frontend
            Map<String, String> response = new HashMap<>();
            response.put("id", preference.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao criar a preferência: " + e.getMessage()));
        }
    }
}
