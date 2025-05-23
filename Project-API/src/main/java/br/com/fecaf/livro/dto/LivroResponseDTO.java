package br.com.fecaf.livro.dto;

import br.com.fecaf.autor.dto.AutorDTO;
import br.com.fecaf.categoria.dto.CategoriaDTO;
import java.util.List;

public class LivroResponseDTO {
    private Long id;
    private String titulo;
    private String isbn;
    private String editora;
    private Integer anoPublicacao;
    private String genero;
    private Integer numeroCopias;
    private Integer numeroCopiasDisponiveis;
    private List<AutorDTO> autores; // Lista de DTOs de Autores
    private CategoriaDTO categoria; // DTO de Categoria

    public LivroResponseDTO(Long id, String titulo, String isbn, String editora, Integer anoPublicacao, String genero, Integer numeroCopias, Integer numeroCopiasDisponiveis, List<AutorDTO> autores, CategoriaDTO categoria) {
        this.id = id;
        this.titulo = titulo;
        this.isbn = isbn;
        this.editora = editora;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
        this.numeroCopias = numeroCopias;
        this.numeroCopiasDisponiveis = numeroCopiasDisponiveis;
        this.autores = autores;
        this.categoria = categoria;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getIsbn() { return isbn; }
    public String getEditora() { return editora; }
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public String getGenero() { return genero; }
    public Integer getNumeroCopias() { return numeroCopias; }
    public Integer getNumeroCopiasDisponiveis() { return numeroCopiasDisponiveis; }
    public List<AutorDTO> getAutores() { return autores; }
    public CategoriaDTO getCategoria() { return categoria; }
}