package br.com.fecaf.autor.controller;

import br.com.fecaf.autor.model.Autor;
import br.com.fecaf.autor.service.AutorService;
import br.com.fecaf.autor.dto.AutorDTO;
import br.com.fecaf.livro.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private static final Logger logger = LoggerFactory.getLogger(AutorController.class);

    @Autowired
    private AutorService autorService;

    @GetMapping
    public ResponseEntity<List<AutorDTO>> listarAutores() {
        List<Autor> autores = autorService.listarAutores();
        List<AutorDTO> autoresDTO = autores.stream()
                .map(autor -> new AutorDTO(autor.getId(), autor.getNome()))
                .collect(Collectors.toList());
        logger.info("AutorController.listarAutores(): {} autores encontrados.", autoresDTO.size());
        return ResponseEntity.ok(autoresDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> buscarAutorPorId(@PathVariable Long id) {
        try {
            Autor autor = autorService.buscarAutorPorId(id);
            logger.info("AutorController.buscarAutorPorId(): Autor encontrado - ID={}", autor.getId());
            return ResponseEntity.ok(new AutorDTO(autor.getId(), autor.getNome()));
        } catch (NotFoundException e) {
            logger.warn("AutorController.buscarAutorPorId(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<AutorDTO> salvarAutor(@RequestBody Autor autor) {
        if (autor == null || autor.getNome() == null || autor.getNome().trim().isEmpty()) {
            logger.error("AutorController.salvarAutor(): Nome do autor é obrigatório.");
            return ResponseEntity.badRequest().build();
        }
        try {
            Autor autorSalvo = autorService.salvarAutor(autor);
            logger.info("AutorController.salvarAutor(): Autor salvo - ID={}", autorSalvo.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new AutorDTO(autorSalvo.getId(), autorSalvo.getNome()));
        } catch (IllegalArgumentException e) {
            logger.error("AutorController.salvarAutor(): Erro de validação: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("AutorController.salvarAutor(): Erro ao salvar autor: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorDTO> atualizarAutor(@PathVariable Long id, @RequestBody Autor autorAtualizado) {
        if (autorAtualizado == null || autorAtualizado.getNome() == null || autorAtualizado.getNome().trim().isEmpty()) {
            logger.error("AutorController.atualizarAutor(): Nome do autor é obrigatório.");
            return ResponseEntity.badRequest().build();
        }
        try {
            Autor autor = autorService.atualizarAutor(id, autorAtualizado);
            logger.info("AutorController.atualizarAutor(): Autor atualizado - ID={}", autor.getId());
            return ResponseEntity.ok(new AutorDTO(autor.getId(), autor.getNome()));
        } catch (NotFoundException e) {
            logger.warn("AutorController.atualizarAutor(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            logger.error("AutorController.atualizarAutor(): Erro de validação: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("AutorController.atualizarAutor(): Erro ao atualizar autor: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAutor(@PathVariable Long id) {
        try {
            autorService.deletarAutor(id);
            logger.info("AutorController.deletarAutor(): Autor deletado - ID={}", id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            logger.warn("AutorController.deletarAutor(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            logger.error("AutorController.deletarAutor(): Erro de regra de negócio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("AutorController.deletarAutor(): Erro ao deletar autor: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}