package com.slaivideos.controller;

import com.slaivideos.model.ContactRequest;
import com.slaivideos.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/contact")
@CrossOrigin(origins = "*")  // 🔹 Permitir requisições de qualquer origem
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<String> receberMensagem(@RequestBody ContactRequest request) {
        try {
            String resposta = contactService.salvarMensagem(request);
            return ResponseEntity.ok(resposta);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao processar a mensagem.");
        }
    }
}
