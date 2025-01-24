package com.slaivideos.controller;

import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Preference;
import com.mercadopago.resources.datastructures.preference.Item;
import com.mercadopago.resources.datastructures.preference.Payer;
import com.slaivideos.model.CheckoutRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*") // Permite chamadas de qualquer origem
public class CheckoutController {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostMapping("/checkout")
    public ResponseEntity<?> createPreference(@RequestBody CheckoutRequest request) {
        try {
            MercadoPago.SDK.setAccessToken(accessToken);

            // Criando um item de pagamento
            Item item = new Item()
                    .setTitle(request.getTitle())
                    .setQuantity(1)
                    .setCurrencyId("BRL")
                    .setUnitPrice((float) request.getPrice());

            // Criando um comprador (Payer)
            Payer payer = new Payer();
            payer.setEmail("comprador@email.com");

            // Criando a preferência de pagamento
            Preference preference = new Preference();
            preference.appendItem(item);
            preference.setPayer(payer);
            preference.save();

            // Retornando o ID da preferência para o frontend
            Map<String, String> response = new HashMap<>();
            response.put("id", preference.getId());

            return ResponseEntity.ok(response);

        } catch (MPException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao criar a preferência: " + e.getMessage()));
        }
    }
}
