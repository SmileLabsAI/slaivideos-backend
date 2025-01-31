package com.slaivideos.controller;

import com.slaivideos.service.ContactService;
import com.slaivideos.model.ContactRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> handleContact(@RequestBody ContactRequest request) {
        if (request.getNome() == null || request.getEmail() == null || request.getMensagem() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Todos os campos são obrigatórios"));
        }

        // Agora, salvamos a mensagem no Supabase!
        String resultado = contactService.salvarMensagem(request);

        if (resultado.contains("Erro")) {
            return ResponseEntity.status(500).body(Map.of("error", resultado));
        }

        return ResponseEntity.ok(Map.of("message", "Mensagem recebida e salva com sucesso!"));
    }
}
