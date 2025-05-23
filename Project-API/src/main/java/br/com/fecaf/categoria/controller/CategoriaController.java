package br.com.fecaf.categoria.controller;

import br.com.fecaf.categoria.model.Categoria;
import br.com.fecaf.categoria.service.CategoriaService;
import br.com.fecaf.categoria.dto.CategoriaDTO;
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
@RequestMapping("/api/categorias")
public class CategoriaController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        List<Categoria> categorias = categoriaService.listarCategorias();
        List<CategoriaDTO> categoriasDTO = categorias.stream()
                .map(categoria -> new CategoriaDTO(categoria.getId(), categoria.getNome()))
                .collect(Collectors.toList());
        logger.info("CategoriaController.listarCategorias(): {} categorias encontradas.", categoriasDTO.size());
        return ResponseEntity.ok(categoriasDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> buscarCategoriaPorId(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.buscarCategoriaPorId(id);
            logger.info("CategoriaController.buscarCategoriaPorId(): Categoria encontrada - ID={}", categoria.getId());
            return ResponseEntity.ok(new CategoriaDTO(categoria.getId(), categoria.getNome()));
        } catch (NotFoundException e) {
            logger.warn("CategoriaController.buscarCategoriaPorId(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> salvarCategoria(@RequestBody Categoria categoria) {
        if (categoria == null || categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            logger.error("CategoriaController.salvarCategoria(): Nome da categoria é obrigatório.");
            return ResponseEntity.badRequest().build();
        }
        try {
            Categoria categoriaSalva = categoriaService.salvarCategoria(categoria);
            logger.info("CategoriaController.salvarCategoria(): Categoria salva - ID={}", categoriaSalva.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new CategoriaDTO(categoriaSalva.getId(), categoriaSalva.getNome()));
        } catch (IllegalArgumentException e) {
            logger.error("CategoriaController.salvarCategoria(): Erro de validação: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("CategoriaController.salvarCategoria(): Erro ao salvar categoria: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> atualizarCategoria(@PathVariable Long id, @RequestBody Categoria categoriaAtualizada) {
        if (categoriaAtualizada == null || categoriaAtualizada.getNome() == null || categoriaAtualizada.getNome().trim().isEmpty()) {
            logger.error("CategoriaController.atualizarCategoria(): Nome da categoria é obrigatório.");
            return ResponseEntity.badRequest().build();
        }
        try {
            Categoria categoria = categoriaService.atualizarCategoria(id, categoriaAtualizada);
            logger.info("CategoriaController.atualizarCategoria(): Categoria atualizada - ID={}", categoria.getId());
            return ResponseEntity.ok(new CategoriaDTO(categoria.getId(), categoria.getNome()));
        } catch (NotFoundException e) {
            logger.warn("CategoriaController.atualizarCategoria(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            logger.error("CategoriaController.atualizarCategoria(): Erro de validação: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("CategoriaController.atualizarCategoria(): Erro ao atualizar categoria: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {
        try {
            categoriaService.deletarCategoria(id);
            logger.info("CategoriaController.deletarCategoria(): Categoria deletada - ID={}", id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            logger.warn("CategoriaController.deletarCategoria(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            logger.error("CategoriaController.deletarCategoria(): Erro de regra de negócio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("CategoriaController.deletarCategoria(): Erro ao deletar categoria: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}