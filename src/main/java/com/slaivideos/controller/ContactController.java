package com.slaivideos.controller;

import com.slaivideos.service.ContactService;
import com.slaivideos.model.ContactRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> handleContact(@RequestBody Map<String, String> request) {
        String nome = request.get("name");
        String email = request.get("email");
        String mensagem = request.get("mensagem");

        if (name == null || email == null || mensagem == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Todos os campos são obrigatórios"));
        }

        // ✅ Restaurando logs para debug
        System.out.println("Nova mensagem recebida:");
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Mensagem: " + mensagem);

        // ✅ Criando objeto de requisição manualmente para manter compatibilidade
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setName(name);
        contactRequest.setEmail(email);
        contactRequest.setMensagem(mensagem);

        // ✅ Enviando a mensagem ao Supabase
        String resultado = contactService.salvarMensagem(contactRequest);

        if (resultado.contains("Erro")) {
            return ResponseEntity.status(500).body(Map.of("error", resultado));
        }

        return ResponseEntity.ok(Map.of("message", "Mensagem recebida e salva com sucesso!"));
    }
}
