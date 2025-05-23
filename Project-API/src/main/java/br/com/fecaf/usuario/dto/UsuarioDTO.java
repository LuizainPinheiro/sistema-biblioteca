package br.com.fecaf.usuario.dto;

import java.time.LocalDate;

public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private LocalDate dataRegistro;
    private String status;

    public UsuarioDTO(Long id, String nome, String email, String telefone, String endereco, LocalDate dataRegistro, String status) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.dataRegistro = dataRegistro;
        this.status = status;
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getEndereco() { return endereco; }
    public LocalDate getDataRegistro() { return dataRegistro; }
    public String getStatus() { return status; }
}