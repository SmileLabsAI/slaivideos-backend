package com.slaivideos.dto;

public class UserRequestDTO {
    private String email;
    private String senha;

    // Construtor padrão necessário para JSON deserializar corretamente
    public UserRequestDTO() {}

    public UserRequestDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
