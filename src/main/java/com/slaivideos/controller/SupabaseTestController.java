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
    public ResponseEntity<?> listarUsuarios() { // ❌ Removemos `throws IOException`
        String usuarios = supabaseUserService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody UserRequestDTO novoUsuario) { // ❌ Removemos `throws IOException`
        if (novoUsuario.getNome() == null || novoUsuario.getEmail() == null || novoUsuario.getSenha() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: Campos obrigatórios (nome, email, senha) não podem ser nulos.");
        }

        String resultado = supabaseUserService.criarUsuario(
                novoUsuario.getNome(),
                novoUsuario.getEmail(),
                novoUsuario.getSenha()
        );
        return ResponseEntity.ok(resultado);
    }
}
