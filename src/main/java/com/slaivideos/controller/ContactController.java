package com.slaivideos.controller;

import com.slaivideos.model.ContactRequest;
import com.slaivideos.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
@CrossOrigin(origins = {"https://smilelabsai.github.io", "https://slaivideos-backend-1.onrender.com"}) // Permitindo apenas origens seguras
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<String> receberMensagem(@RequestBody ContactRequest request) {
        try {
            String resposta = contactService.salvarMensagem(request);
            if (resposta.startsWith("Erro")) {
                return ResponseEntity.status(500).body(resposta); // Se houver erro, retorna 500
            }
            return ResponseEntity.ok(resposta); // Se tudo der certo, retorna 200
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao processar a solicitação: " + e.getMessage());
        }
    }
}
