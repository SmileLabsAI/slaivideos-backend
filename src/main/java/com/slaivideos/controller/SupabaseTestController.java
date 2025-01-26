package com.slaivideos.controller;

import com.slaivideos.service.SupabaseUserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class SupabaseTestController {

    private final SupabaseUserService supabaseUserService;

    @Autowired
    public SupabaseTestController(SupabaseUserService supabaseUserService) {
        this.supabaseUserService = supabaseUserService;
    }

    @GetMapping
    public String listarUsuarios() throws IOException {
        return supabaseUserService.listarUsuarios();
    }

    @PostMapping
    public String criarUsuario(@RequestBody Map<String, String> novoUsuario) throws IOException {
        String nome = novoUsuario.get("nome");
        String email = novoUsuario.get("email");
        String senha = novoUsuario.get("senha");

        if (nome == null || email == null || senha == null) {
            return "Erro: Campos obrigatórios (nome, email, senha) não podem ser nulos.";
        }

        return supabaseUserService.criarUsuario(nome, email, senha);
    }
}
