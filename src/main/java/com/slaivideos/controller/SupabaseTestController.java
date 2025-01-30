package com.slaivideos.controller;

import com.slaivideos.service.SupabaseUserService;
import com.slaivideos.dto.UserRequestDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/usuarios")
public class SupabaseTestController {

    private final SupabaseUserService supabaseUserService;

    @Autowired
    public SupabaseTestController(SupabaseUserService supabaseUserService) {
        this.supabaseUserService = supabaseUserService;
    }

    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        try {
            String usuarios = supabaseUserService.listarUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar usuários: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody UserRequestDTO novoUsuario) {
        if (novoUsuario.getNome() == null || novoUsuario.getEmail() == null || novoUsuario.getSenha() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: Campos obrigatórios (nome, email, senha) não podem ser nulos.");
        }

        try {
            String resultado = supabaseUserService.criarUsuario(
                    novoUsuario.getNome(),
                    novoUsuario.getEmail(),
                    novoUsuario.getSenha()
            );

            // ✅ Redirecionamento para página de sucesso
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/sucesso.html")
                    .build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }
}
