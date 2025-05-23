package br.com.fecaf.livro.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class LivroRequestDTO {
    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotBlank(message = "O ISBN é obrigatório")
    @Size(min = 10, max = 13, message = "O ISBN deve ter entre 10 e 13 caracteres")
    private String isbn;

    @NotBlank(message = "A editora é obrigatória")
    private String editora;

    private Integer anoPublicacao; // Pode ser null

    @NotBlank(message = "O gênero é obrigatório")
    private String genero;

    @NotNull(message = "O número de cópias é obrigatório")
    @Positive(message = "O número de cópias deve ser positivo")
    private Integer numeroCopias;



    @NotNull(message = "A lista de IDs de autores é obrigatória")
    @Size(min = 1, message = "O livro deve ter pelo menos um autor")
    private List<Long> autoresIds;

    @NotNull(message = "O ID da categoria é obrigatório")
    @Positive(message = "O ID da categoria deve ser positivo")
    private Long categoriaId;

    public LivroRequestDTO() {}

    public LivroRequestDTO(String titulo, String isbn, String editora, Integer anoPublicacao, String genero, Integer numeroCopias, List<Long> autoresIds, Long categoriaId) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.editora = editora;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
        this.numeroCopias = numeroCopias;
        this.autoresIds = autoresIds;
        this.categoriaId = categoriaId;
    }

    // Getters e Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public Integer getNumeroCopias() { return numeroCopias; }
    public void setNumeroCopias(Integer numeroCopias) { this.numeroCopias = numeroCopias; }
    public List<Long> getAutoresIds() { return autoresIds; }
    public void setAutoresIds(List<Long> autoresIds) { this.autoresIds = autoresIds; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
}