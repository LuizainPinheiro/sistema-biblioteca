package br.com.fecaf.emprestimo.dto;

import br.com.fecaf.livro.dto.LivroResponseDTO; // Pode usar um DTO mais simples para Livro se LivroResponseDTO for muito detalhado
import br.com.fecaf.usuario.dto.UsuarioDTO; // Pode usar um DTO mais simples para Usuario
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmprestimoResponseDTO {
    private Long id;
    private LivroResponseDTO livro; // Ou um DTO mais simples: LivroSimplesDTO
    private UsuarioDTO usuario; // Ou um DTO mais simples: UsuarioSimplesDTO
    private LocalDateTime dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;

    public EmprestimoResponseDTO(Long id, LivroResponseDTO livro, UsuarioDTO usuario, LocalDateTime dataEmprestimo, LocalDate dataDevolucaoPrevista, LocalDate dataDevolucaoReal) {
        this.id = id;
        this.livro = livro;
        this.usuario = usuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    // Getters
    public Long getId() { return id; }
    public LivroResponseDTO getLivro() { return livro; }
    public UsuarioDTO getUsuario() { return usuario; }
    public LocalDateTime getDataEmprestimo() { return dataEmprestimo; }
    public LocalDate getDataDevolucaoPrevista() { return dataDevolucaoPrevista; }
    public LocalDate getDataDevolucaoReal() { return dataDevolucaoReal; }
}