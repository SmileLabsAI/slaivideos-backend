package com.slaivideos.controller;

import com.slaivideos.model.ContactRequest;
import com.slaivideos.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
@CrossOrigin(origins = "*")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<String> receberMensagem(@RequestBody ContactRequest request) {
        String resposta = contactService.salvarMensagem(request);
        return ResponseEntity.ok(resposta);
    }
}
