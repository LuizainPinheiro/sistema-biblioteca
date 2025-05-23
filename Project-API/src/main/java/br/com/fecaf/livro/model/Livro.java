package br.com.fecaf.livro.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import br.com.fecaf.autor.model.Autor;
import br.com.fecaf.categoria.model.Categoria;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column
    private String editora;

    @Column(name = "ano_publicacao")
    private Integer anoPublicacao;

    @Column
    private String genero;

    @Column(name = "numero_copias")
    private Integer numeroCopias;

    @Column(name = "numero_copias_disponiveis")
    private Integer numeroCopiasDisponiveis;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    @JsonManagedReference("livro-autores")
    private Set<Autor> autores = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonManagedReference("livro-categoria")
    private Categoria categoria;

    public Livro() {}

    public Livro(String titulo, String isbn, String editora, Integer anoPublicacao, String genero, Integer numeroCopias, Integer numeroCopiasDisponiveis) {
        this.titulo = titulo;
        this.isbn = isbn;
        this.editora = editora;
        this.anoPublicacao = anoPublicacao;
        this.genero = genero;
        this.numeroCopias = numeroCopias;
        this.numeroCopiasDisponiveis = numeroCopiasDisponiveis;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public Integer getNumeroCopiasDisponiveis() { return numeroCopiasDisponiveis; }
    public void setNumeroCopiasDisponiveis(Integer numeroCopiasDisponiveis) { this.numeroCopiasDisponiveis = numeroCopiasDisponiveis; }
    public Set<Autor> getAutores() { return autores; }
    public void setAutores(Set<Autor> autores) { this.autores = autores; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public void addAutor(Autor autor) {
        this.autores.add(autor);
        autor.getLivros().add(this);
    }

    public void removeAutor(Autor autor) {
        this.autores.remove(autor);
        autor.getLivros().remove(this);
    }

    @PrePersist
    public void prePersist() {
        if (this.numeroCopiasDisponiveis == null) {
            this.numeroCopiasDisponiveis = this.numeroCopias;
        }
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", isbn='" + isbn + '\'' +
                ", editora='" + editora + '\'' +
                ", anoPublicacao=" + anoPublicacao +
                ", genero='" + genero + '\'' +
                ", numeroCopias=" + numeroCopias +
                ", numeroCopiasDisponiveis=" + numeroCopiasDisponiveis +
                '}';
    }
}