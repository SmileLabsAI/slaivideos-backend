package com.slaivideos.controller;

import com.slaivideos.dto.UserRequestDTO;
import com.slaivideos.dto.LoginResponseDTO;
import com.slaivideos.service.SupabaseUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "https://smilelabsai.github.io") // Adiciona o CORS diretamente no controller
public class UserController {

    private final SupabaseUserService supabaseUserService;

    public UserController(SupabaseUserService supabaseUserService) {
        this.supabaseUserService = supabaseUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO request) {
        if (request.getEmail() == null || request.getSenha() == null) {
            return ResponseEntity.badRequest().body("Email e senha são obrigatórios.");
        }
        return supabaseUserService.loginUsuario(request.getEmail(), request.getSenha());
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(@RequestBody UserRequestDTO request) {
        // Implementar lógica de cadastro no Supabase se necessário
        return ResponseEntity.ok("Cadastro ainda não implementado.");
    }

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }
}
