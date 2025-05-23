package br.com.fecaf.livro.controller;

import br.com.fecaf.autor.model.Autor;
import br.com.fecaf.categoria.model.Categoria;
import br.com.fecaf.livro.model.Livro;
import br.com.fecaf.livro.service.LivroService;
import br.com.fecaf.livro.dto.LivroRequestDTO;
import br.com.fecaf.livro.dto.LivroResponseDTO;
import br.com.fecaf.autor.dto.AutorDTO;
import br.com.fecaf.categoria.dto.CategoriaDTO;
import br.com.fecaf.livro.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid; // Para validação de DTO

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private static final Logger logger = LoggerFactory.getLogger(LivroController.class);

    @Autowired
    private LivroService livroService;

    @GetMapping
    public ResponseEntity<List<LivroResponseDTO>> listarLivros() {
        List<Livro> livros = livroService.listarLivros();
        List<LivroResponseDTO> livrosDTO = livros.stream()
                .map(livro -> new LivroResponseDTO(
                        livro.getId(),
                        livro.getTitulo(),
                        livro.getIsbn(),
                        livro.getEditora(),
                        livro.getAnoPublicacao(),
                        livro.getGenero(),
                        livro.getNumeroCopias(),
                        livro.getNumeroCopiasDisponiveis(),
                        livro.getAutores().stream()
                                .map(autor -> new AutorDTO(autor.getId(), autor.getNome()))
                                .collect(Collectors.toList()),
                        livro.getCategoria() != null ? new CategoriaDTO(livro.getCategoria().getId(), livro.getCategoria().getNome()) : null
                ))
                .collect(Collectors.toList());
        logger.info("LivroController.listarLivros(): {} livros encontrados.", livrosDTO.size());
        return ResponseEntity.ok(livrosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarLivroPorId(@PathVariable Long id) {
        try {
            Livro livro = livroService.buscarLivroPorId(id);
            logger.info("LivroController.buscarLivroPorId(): Livro encontrado - ID={}", livro.getId());
            LivroResponseDTO livroDTO = new LivroResponseDTO(
                    livro.getId(),
                    livro.getTitulo(),
                    livro.getIsbn(),
                    livro.getEditora(),
                    livro.getAnoPublicacao(),
                    livro.getGenero(),
                    livro.getNumeroCopias(),
                    livro.getNumeroCopiasDisponiveis(),
                    livro.getAutores().stream()
                            .map(autor -> new AutorDTO(autor.getId(), autor.getNome()))
                            .collect(Collectors.toList()),
                    livro.getCategoria() != null ? new CategoriaDTO(livro.getCategoria().getId(), livro.getCategoria().getNome()) : null
            );
            return ResponseEntity.ok(livroDTO);
        } catch (NotFoundException e) {
            logger.warn("LivroController.buscarLivroPorId(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<LivroResponseDTO> salvarLivro(@Valid @RequestBody LivroRequestDTO livroRequestDTO) {
        try {
            // Mapeia DTO para Entidade
            Livro livro = new Livro();
            livro.setTitulo(livroRequestDTO.getTitulo());
            livro.setIsbn(livroRequestDTO.getIsbn());
            livro.setEditora(livroRequestDTO.getEditora());
            livro.setAnoPublicacao(livroRequestDTO.getAnoPublicacao());
            livro.setGenero(livroRequestDTO.getGenero());
            livro.setNumeroCopias(livroRequestDTO.getNumeroCopias());


            livro.setAutores(livroRequestDTO.getAutoresIds().stream()
                    .map(id -> {
                        Autor autor = new Autor();
                        autor.setId(id);
                        return autor;
                    }).collect(Collectors.toSet()));


            Categoria categoria = new Categoria();
            categoria.setId(livroRequestDTO.getCategoriaId());
            livro.setCategoria(categoria);

            Livro livroSalvo = livroService.salvarLivro(livro);

            logger.info("LivroController.salvarLivro(): Livro salvo - ID={}", livroSalvo.getId());

            LivroResponseDTO livroDTO = new LivroResponseDTO(
                    livroSalvo.getId(),
                    livroSalvo.getTitulo(),
                    livroSalvo.getIsbn(),
                    livroSalvo.getEditora(),
                    livroSalvo.getAnoPublicacao(),
                    livroSalvo.getGenero(),
                    livroSalvo.getNumeroCopias(),
                    livroSalvo.getNumeroCopiasDisponiveis(),
                    livroSalvo.getAutores().stream()
                            .map(autor -> new AutorDTO(autor.getId(), autor.getNome()))
                            .collect(Collectors.toList()),
                    livroSalvo.getCategoria() != null ? new CategoriaDTO(livroSalvo.getCategoria().getId(), livroSalvo.getCategoria().getNome()) : null
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(livroDTO);
        } catch (NotFoundException | IllegalArgumentException e) {
            logger.error("LivroController.salvarLivro(): Erro de validação/negócio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("LivroController.salvarLivro(): Erro interno ao salvar livro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizarLivro(@PathVariable Long id, @Valid @RequestBody LivroRequestDTO livroRequestDTO) {
        try {

            Livro livro = new Livro();
            livro.setTitulo(livroRequestDTO.getTitulo());
            livro.setIsbn(livroRequestDTO.getIsbn());
            livro.setEditora(livroRequestDTO.getEditora());
            livro.setAnoPublicacao(livroRequestDTO.getAnoPublicacao());
            livro.setGenero(livroRequestDTO.getGenero());
            livro.setNumeroCopias(livroRequestDTO.getNumeroCopias());
            livro.setNumeroCopiasDisponiveis(livroRequestDTO.getNumeroCopias());
            livro.setAutores(livroRequestDTO.getAutoresIds().stream()
                    .map(autorId -> {
                        Autor autor = new Autor();
                        autor.setId(autorId);
                        return autor;
                    }).collect(Collectors.toSet()));

            Categoria categoria = new Categoria();
            categoria.setId(livroRequestDTO.getCategoriaId());
            livro.setCategoria(categoria);

            Livro livroAtualizado = livroService.atualizarLivro(id, livro);

            logger.info("LivroController.atualizarLivro(): Livro atualizado - ID={}", livroAtualizado.getId());

            LivroResponseDTO livroDTO = new LivroResponseDTO(
                    livroAtualizado.getId(),
                    livroAtualizado.getTitulo(),
                    livroAtualizado.getIsbn(),
                    livroAtualizado.getEditora(),
                    livroAtualizado.getAnoPublicacao(),
                    livroAtualizado.getGenero(),
                    livroAtualizado.getNumeroCopias(),
                    livroAtualizado.getNumeroCopiasDisponiveis(),
                    livroAtualizado.getAutores().stream()
                            .map(autor -> new AutorDTO(autor.getId(), autor.getNome()))
                            .collect(Collectors.toList()),
                    livroAtualizado.getCategoria() != null ? new CategoriaDTO(livroAtualizado.getCategoria().getId(), livroAtualizado.getCategoria().getNome()) : null
            );
            return ResponseEntity.ok(livroDTO);
        } catch (NotFoundException e) {
            logger.warn("LivroController.atualizarLivro(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            logger.error("LivroController.atualizarLivro(): Erro de validação/negócio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("LivroController.atualizarLivro(): Erro interno ao atualizar livro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) {
        try {
            livroService.deletarLivro(id);
            logger.info("LivroController.deletarLivro(): Livro deletado - ID={}", id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            logger.warn("LivroController.deletarLivro(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("LivroController.deletarLivro(): Erro ao deletar livro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}