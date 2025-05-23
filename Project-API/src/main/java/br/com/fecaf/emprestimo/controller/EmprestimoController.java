package br.com.fecaf.emprestimo.controller;

import br.com.fecaf.autor.dto.AutorDTO;
import br.com.fecaf.categoria.dto.CategoriaDTO;
import br.com.fecaf.emprestimo.model.Emprestimo;
import br.com.fecaf.emprestimo.service.EmprestimoService;
import br.com.fecaf.emprestimo.dto.EmprestimoRequestDTO;
import br.com.fecaf.emprestimo.dto.EmprestimoResponseDTO;
import br.com.fecaf.livro.exception.NotFoundException;
import br.com.fecaf.livro.dto.LivroResponseDTO; // Para o DTO de livro dentro do emprestimo
import br.com.fecaf.usuario.dto.UsuarioDTO; // Para o DTO de usuario dentro do emprestimo
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private static final Logger logger = LoggerFactory.getLogger(EmprestimoController.class);

    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping
    public ResponseEntity<List<EmprestimoResponseDTO>> listarEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimos();
        List<EmprestimoResponseDTO> emprestimosDTO = emprestimos.stream()
                .map(e -> new EmprestimoResponseDTO(
                        e.getId(),
                        e.getLivro() != null ? new LivroResponseDTO(
                                e.getLivro().getId(), e.getLivro().getTitulo(), e.getLivro().getIsbn(),
                                e.getLivro().getEditora(), e.getLivro().getAnoPublicacao(), e.getLivro().getGenero(),
                                e.getLivro().getNumeroCopias(), e.getLivro().getNumeroCopiasDisponiveis(),
                                e.getLivro().getAutores().stream().map(a -> new AutorDTO(a.getId(), a.getNome())).collect(Collectors.toList()),
                                e.getLivro().getCategoria() != null ? new CategoriaDTO(e.getLivro().getCategoria().getId(), e.getLivro().getCategoria().getNome()) : null
                        ) : null,
                        e.getUsuario() != null ? new UsuarioDTO(
                                e.getUsuario().getId(), e.getUsuario().getNome(), e.getUsuario().getEmail(),
                                e.getUsuario().getTelefone(), e.getUsuario().getEndereco(),
                                e.getUsuario().getDataRegistro(), e.getUsuario().getStatus()
                        ) : null,
                        e.getDataEmprestimo(),
                        e.getDataDevolucaoPrevista(),
                        e.getDataDevolucaoReal()
                )).collect(Collectors.toList());
        logger.info("EmprestimoController.listarEmprestimos(): {} empréstimos encontrados.", emprestimosDTO.size());
        return ResponseEntity.ok(emprestimosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoResponseDTO> buscarEmprestimoPorId(@PathVariable Long id) {
        try {
            Emprestimo e = emprestimoService.buscarEmprestimoPorId(id);
            logger.info("EmprestimoController.buscarEmprestimoPorId(): Empréstimo encontrado - ID={}", e.getId());
            EmprestimoResponseDTO emprestimoDTO = new EmprestimoResponseDTO(
                    e.getId(),
                    e.getLivro() != null ? new LivroResponseDTO(
                            e.getLivro().getId(), e.getLivro().getTitulo(), e.getLivro().getIsbn(),
                            e.getLivro().getEditora(), e.getLivro().getAnoPublicacao(), e.getLivro().getGenero(),
                            e.getLivro().getNumeroCopias(), e.getLivro().getNumeroCopiasDisponiveis(),
                            e.getLivro().getAutores().stream().map(a -> new AutorDTO(a.getId(), a.getNome())).collect(Collectors.toList()),
                            e.getLivro().getCategoria() != null ? new CategoriaDTO(e.getLivro().getCategoria().getId(), e.getLivro().getCategoria().getNome()) : null
                    ) : null,
                    e.getUsuario() != null ? new UsuarioDTO(
                            e.getUsuario().getId(), e.getUsuario().getNome(), e.getUsuario().getEmail(),
                            e.getUsuario().getTelefone(), e.getUsuario().getEndereco(),
                            e.getUsuario().getDataRegistro(), e.getUsuario().getStatus()
                    ) : null,
                    e.getDataEmprestimo(),
                    e.getDataDevolucaoPrevista(),
                    e.getDataDevolucaoReal()
            );
            return ResponseEntity.ok(emprestimoDTO);
        } catch (NotFoundException e) {
            logger.warn("EmprestimoController.buscarEmprestimoPorId(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<EmprestimoResponseDTO> realizarEmprestimo(@Valid @RequestBody EmprestimoRequestDTO emprestimoRequestDTO) {
        try {
            Emprestimo emprestimoSalvo = emprestimoService.realizarEmprestimo(
                    emprestimoRequestDTO.getLivroId(),
                    emprestimoRequestDTO.getUsuarioId(),
                    emprestimoRequestDTO.getDataDevolucaoPrevista()
            );
            logger.info("EmprestimoController.realizarEmprestimo(): Empréstimo salvo - ID={}", emprestimoSalvo.getId());

            EmprestimoResponseDTO emprestimoDTO = new EmprestimoResponseDTO(
                    emprestimoSalvo.getId(),
                    new LivroResponseDTO( // Reconstruindo DTOs aninhados para a resposta
                            emprestimoSalvo.getLivro().getId(), emprestimoSalvo.getLivro().getTitulo(), emprestimoSalvo.getLivro().getIsbn(),
                            emprestimoSalvo.getLivro().getEditora(), emprestimoSalvo.getLivro().getAnoPublicacao(), emprestimoSalvo.getLivro().getGenero(),
                            emprestimoSalvo.getLivro().getNumeroCopias(), emprestimoSalvo.getLivro().getNumeroCopiasDisponiveis(),
                            emprestimoSalvo.getLivro().getAutores().stream().map(a -> new AutorDTO(a.getId(), a.getNome())).collect(Collectors.toList()),
                            emprestimoSalvo.getLivro().getCategoria() != null ? new CategoriaDTO(emprestimoSalvo.getLivro().getCategoria().getId(), emprestimoSalvo.getLivro().getCategoria().getNome()) : null
                    ),
                    new UsuarioDTO(
                            emprestimoSalvo.getUsuario().getId(), emprestimoSalvo.getUsuario().getNome(), emprestimoSalvo.getUsuario().getEmail(),
                            emprestimoSalvo.getUsuario().getTelefone(), emprestimoSalvo.getUsuario().getEndereco(),
                            emprestimoSalvo.getUsuario().getDataRegistro(), emprestimoSalvo.getUsuario().getStatus()
                    ),
                    emprestimoSalvo.getDataEmprestimo(),
                    emprestimoSalvo.getDataDevolucaoPrevista(),
                    emprestimoSalvo.getDataDevolucaoReal()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoDTO);
        } catch (NotFoundException | IllegalArgumentException e) {
            logger.error("EmprestimoController.realizarEmprestimo(): Erro de validação/negócio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("EmprestimoController.realizarEmprestimo(): Erro interno ao salvar empréstimo: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<EmprestimoResponseDTO> registrarDevolucao(@PathVariable Long id) {
        try {
            Emprestimo emprestimoDevolvido = emprestimoService.registrarDevolucao(id);
            logger.info("EmprestimoController.registrarDevolucao(): Empréstimo devolvido - ID={}", emprestimoDevolvido.getId());

            EmprestimoResponseDTO emprestimoDTO = new EmprestimoResponseDTO(
                    emprestimoDevolvido.getId(),
                    new LivroResponseDTO(
                            emprestimoDevolvido.getLivro().getId(), emprestimoDevolvido.getLivro().getTitulo(), emprestimoDevolvido.getLivro().getIsbn(),
                            emprestimoDevolvido.getLivro().getEditora(), emprestimoDevolvido.getLivro().getAnoPublicacao(), emprestimoDevolvido.getLivro().getGenero(),
                            emprestimoDevolvido.getLivro().getNumeroCopias(), emprestimoDevolvido.getLivro().getNumeroCopiasDisponiveis(),
                            emprestimoDevolvido.getLivro().getAutores().stream().map(a -> new AutorDTO(a.getId(), a.getNome())).collect(Collectors.toList()),
                            emprestimoDevolvido.getLivro().getCategoria() != null ? new CategoriaDTO(emprestimoDevolvido.getLivro().getCategoria().getId(), emprestimoDevolvido.getLivro().getCategoria().getNome()) : null
                    ),
                    new UsuarioDTO(
                            emprestimoDevolvido.getUsuario().getId(), emprestimoDevolvido.getUsuario().getNome(), emprestimoDevolvido.getUsuario().getEmail(),
                            emprestimoDevolvido.getUsuario().getTelefone(), emprestimoDevolvido.getUsuario().getEndereco(),
                            emprestimoDevolvido.getUsuario().getDataRegistro(), emprestimoDevolvido.getUsuario().getStatus()
                    ),
                    emprestimoDevolvido.getDataEmprestimo(),
                    emprestimoDevolvido.getDataDevolucaoPrevista(),
                    emprestimoDevolvido.getDataDevolucaoReal()
            );
            return ResponseEntity.ok(emprestimoDTO);
        } catch (NotFoundException e) {
            logger.warn("EmprestimoController.registrarDevolucao(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            logger.error("EmprestimoController.registrarDevolucao(): Erro de regra de negócio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("EmprestimoController.registrarDevolucao(): Erro interno ao registrar devolução: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmprestimo(@PathVariable Long id) {
        try {
            emprestimoService.deletarEmprestimo(id);
            logger.info("EmprestimoController.deletarEmprestimo(): Empréstimo deletado - ID={}", id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            logger.warn("EmprestimoController.deletarEmprestimo(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            logger.error("EmprestimoController.deletarEmprestimo(): Erro de regra de negócio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("EmprestimoController.deletarEmprestimo(): Erro ao deletar empréstimo: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}