package com.slaivideos.controller;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.slaivideos.model.PaymentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @PostMapping("/process")
    public ResponseEntity<Map<String, String>> processPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            // Configurar Mercado Pago
            MercadoPagoConfig.setAccessToken(accessToken);

            // Criar lista de itens
            List<PreferenceItemRequest> items = new ArrayList<>();
            for (PaymentRequest.Item item : paymentRequest.getItems()) {
                PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                        .title(item.getTitle())
                        .quantity(item.getQuantity())
                        .currencyId("BRL")
                        .unitPrice(BigDecimal.valueOf(item.getUnit_price()))
                        .build();
                items.add(itemRequest);
            }

            // Configurar URLs de retorno
            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success("https://smilelabsai.github.io/SLAIVideos/sucesso.html")
                    .failure("https://smilelabsai.github.io/SLAIVideos/falha.html")
                    .pending("https://smilelabsai.github.io/SLAIVideos/pendente.html")
                    .build();

            // Criar preferência de pagamento
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            // Retornar `init_point` para frontend
            Map<String, String> response = new HashMap<>();
            response.put("id", preference.getId());
            response.put("init_point", preference.getInitPoint());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao criar preferência: " + e.getMessage()));
        }
    }
}
