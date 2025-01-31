package com.slaivideos.controller;

import com.slaivideos.service.SupabaseUserService;
import com.slaivideos.dto.UserRequestDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@CrossOrigin(
        origins = {
                "https://smilelabsai.github.io",
                "https://slaivideos-backend-1.onrender.com",
                "https://rxqieqpxjztnelrsibqc.supabase.com"
        },
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS}
)
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

            return ResponseEntity.ok(Map.of("message", resultado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    // ✅ Endpoint de Login
    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody UserRequestDTO usuario) {
        if (usuario.getEmail() == null || usuario.getSenha() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Email e senha são obrigatórios.");
        }

        try {
            String resultado = supabaseUserService.loginUsuario(usuario.getEmail(), usuario.getSenha());

            if ("Login bem-sucedido!".equals(resultado)) {
                return ResponseEntity.ok(Map.of("message", resultado));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", resultado));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erro ao processar login: " + e.getMessage()));
        }
    }

    // ✅ Adicionando suporte a OPTIONS para resolver o erro de CORS
    @RequestMapping(value = "/login", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> preflightLogin() {
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/usuarios", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> preflightUsuarios() {
        return ResponseEntity.ok().build();
    }
}
