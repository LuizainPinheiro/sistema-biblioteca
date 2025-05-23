package br.com.fecaf.autor.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import br.com.fecaf.livro.model.Livro;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "autores")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.LAZY)
    @JsonBackReference("livro-autores")
    private Set<Livro> livros = new HashSet<>();

    public Autor() {
    }

    public Autor(String nome) {
        this.nome = nome;
    }

    public Autor(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getBiografia() { return biografia; }
    public void setBiografia(String biografia) { this.biografia = biografia; }
    public Set<Livro> getLivros() { return livros; }
    public void setLivros(Set<Livro> livros) { this.livros = livros; }

    @Override
    public String toString() {
        return "Autor{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", biografia='" + (biografia != null ? biografia.substring(0, Math.min(biografia.length(), 20)) + "..." : "null") + '\'' + // Curta a biografia para o log
                '}';
    }
}