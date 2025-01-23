package com.slaivideos.controller;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Value("${mercadopago.access-token}")
    private String mercadoPagoAccessToken;

    @PostMapping("/create_preference")
    public String createPreference(@RequestBody CheckoutRequest checkoutRequest) {
        try {
            MercadoPagoConfig.setAccessToken(mercadoPagoAccessToken);

            PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                    .title(checkoutRequest.getTitle())
                    .quantity(1)
                    .unitPrice(new BigDecimal(checkoutRequest.getPrice()))
                    .currencyId("BRL")
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(Collections.singletonList(itemRequest))
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            return preference.getId();
        } catch (Exception e) {
            return "Erro ao criar a preferência de pagamento: " + e.getMessage();
        }
    }
}

class CheckoutRequest {
    private String title;
    private String price;

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }
}
