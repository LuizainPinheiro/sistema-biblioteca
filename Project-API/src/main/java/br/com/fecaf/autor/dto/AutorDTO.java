package br.com.fecaf.autor.dto; // Exemplo de pacote

public class AutorDTO {
    private Long id;
    private String nome;

    public AutorDTO() {
    }

    public AutorDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // --- Getters e Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}