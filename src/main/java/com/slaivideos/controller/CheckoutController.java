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
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*") // Permitir chamadas de qualquer origem
public class CheckoutController {

    // Carrega a credencial do Mercado Pago do application.properties
    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostMapping("/checkout")
    public ResponseEntity<?> createPreference(@RequestBody CheckoutRequest request) {
        try {
            // Configura o SDK do Mercado Pago com o Access Token
            MercadoPagoConfig.setAccessToken(accessToken);

            // Cria um item de pagamento
            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title(request.getTitle()) // Nome do produto
                    .quantity(1) // Quantidade
                    .currencyId("BRL") // Moeda
                    .unitPrice(BigDecimal.valueOf(request.getPrice())) // Preço
                    .build();

            // Cria a preferência de pagamento
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(Collections.singletonList(itemRequest))
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            // Retorna o ID da preferência para o frontend iniciar o checkout
            return ResponseEntity.ok(Collections.singletonMap("id", preference.getId()));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("error", "Erro ao criar a preferência: " + e.getMessage()));
        }
    }
}
