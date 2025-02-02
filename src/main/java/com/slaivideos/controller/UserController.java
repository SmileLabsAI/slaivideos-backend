package com.slaivideos.controller;

import com.slaivideos.dto.UserRequestDTO;
import com.slaivideos.service.SupabaseUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "https://smilelabsai.github.io") // 🔥 Garante que o frontend pode chamar a API
public class UserController {

    private final SupabaseUserService supabaseUserService;

    public UserController(SupabaseUserService supabaseUserService) {
        this.supabaseUserService = supabaseUserService;
    }

    // ✅ Login de usuário
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO request) {
        if (request.getEmail() == null || request.getSenha() == null) {
            return ResponseEntity.badRequest().body("Email e senha são obrigatórios.");
        }
        return supabaseUserService.loginUsuario(request.getEmail(), request.getSenha());
    }

    // ✅ Cadastro de usuário
    @PostMapping
    public ResponseEntity<?> cadastro(@RequestBody UserRequestDTO request) {
        if (request.getEmail() == null || request.getSenha() == null) {
            return ResponseEntity.badRequest().body("Email e senha são obrigatórios.");
        }
        return supabaseUserService.criarUsuario(request);
    }

    // ✅ Corrige erro de CORS (OPTIONS request)
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }
}
