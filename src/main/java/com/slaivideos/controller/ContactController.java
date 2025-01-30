package com.slaivideos.controller; // ✅ Corrigido o package

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @PostMapping
    public ResponseEntity<Map<String, String>> handleContact(@RequestBody Map<String, String> request) {
        String nome = request.get("nome");
        String email = request.get("email");
        String mensagem = request.get("mensagem");

        if (nome == null || email == null || mensagem == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Todos os campos são obrigatórios"));
        }

        System.out.println("Nova mensagem recebida:");
        System.out.println("Nome: " + nome);
        System.out.println("Email: " + email);
        System.out.println("Mensagem: " + mensagem);

        return ResponseEntity.ok(Map.of("message", "Mensagem recebida com sucesso!"));
    }
}
