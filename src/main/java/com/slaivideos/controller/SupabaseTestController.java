package com.slaivideos.controller;

import com.slaivideos.service.SupabaseUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/usuarios")
public class SupabaseTestController {

    private final SupabaseUserService supabaseUserService;

    public SupabaseTestController(SupabaseUserService supabaseUserService) {
        this.supabaseUserService = supabaseUserService;
    }

    @GetMapping
    public String listarUsuarios() throws IOException {
        return supabaseUserService.listarUsuarios();
    }
}
