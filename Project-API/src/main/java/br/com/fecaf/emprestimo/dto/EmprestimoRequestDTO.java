package br.com.fecaf.emprestimo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class EmprestimoRequestDTO {
    @NotNull(message = "O ID do livro é obrigatório")
    @Positive(message = "O ID do livro deve ser positivo")
    private Long livroId;

    @NotNull(message = "O ID do usuário é obrigatório")
    @Positive(message = "O ID do usuário deve ser positivo")
    private Long usuarioId;

    @NotNull(message = "A data de devolução prevista é obrigatória")
    private LocalDate dataDevolucaoPrevista;

    // Construtor
    public EmprestimoRequestDTO() {}

    public EmprestimoRequestDTO(Long livroId, Long usuarioId, LocalDate dataDevolucaoPrevista) {
        this.livroId = livroId;
        this.usuarioId = usuarioId;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    // Getters e Setters
    public Long getLivroId() { return livroId; }
    public void setLivroId(Long livroId) { this.livroId = livroId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) { this.dataDevolucaoPrevista = dataDevolucaoPrevista; }
}