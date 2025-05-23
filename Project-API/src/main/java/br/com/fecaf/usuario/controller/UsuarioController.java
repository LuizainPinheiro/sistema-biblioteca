package br.com.fecaf.usuario.controller;

import br.com.fecaf.usuario.model.Usuario;
import br.com.fecaf.usuario.service.UsuarioService;
import br.com.fecaf.usuario.dto.UsuarioDTO;
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
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        List<UsuarioDTO> usuariosDTO = usuarios.stream()
                .map(usuario -> new UsuarioDTO(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getTelefone(),
                        usuario.getEndereco(),
                        usuario.getDataRegistro(),
                        usuario.getStatus()
                )).collect(Collectors.toList());
        logger.info("UsuarioController.listarUsuarios(): {} usuários encontrados.", usuariosDTO.size());
        return ResponseEntity.ok(usuariosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPorId(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.buscarUsuarioPorId(id);
            logger.info("UsuarioController.buscarUsuarioPorId(): Usuário encontrado - ID={}", usuario.getId());
            return ResponseEntity.ok(new UsuarioDTO(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getTelefone(),
                    usuario.getEndereco(),
                    usuario.getDataRegistro(),
                    usuario.getStatus()
            ));
        } catch (NotFoundException e) {
            logger.warn("UsuarioController.buscarUsuarioPorId(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvarUsuario(@RequestBody Usuario usuario) {

        if (usuario == null || usuario.getNome() == null || usuario.getNome().trim().isEmpty() ||
                usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            logger.error("UsuarioController.salvarUsuario(): Nome e Email do usuário são obrigatórios.");
            return ResponseEntity.badRequest().build();
        }
        try {
            Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
            logger.info("UsuarioController.salvarUsuario(): Usuário salvo - ID={}", usuarioSalvo.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioDTO(
                    usuarioSalvo.getId(),
                    usuarioSalvo.getNome(),
                    usuarioSalvo.getEmail(),
                    usuarioSalvo.getTelefone(),
                    usuarioSalvo.getEndereco(),
                    usuarioSalvo.getDataRegistro(),
                    usuarioSalvo.getStatus()
            ));
        } catch (IllegalArgumentException e) {
            logger.error("UsuarioController.salvarUsuario(): Erro de validação: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("UsuarioController.salvarUsuario(): Erro ao salvar usuário: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        if (usuarioAtualizado == null || usuarioAtualizado.getNome() == null || usuarioAtualizado.getNome().trim().isEmpty() ||
                usuarioAtualizado.getEmail() == null || usuarioAtualizado.getEmail().trim().isEmpty()) {
            logger.error("UsuarioController.atualizarUsuario(): Nome e Email do usuário são obrigatórios.");
            return ResponseEntity.badRequest().build();
        }
        try {
            Usuario usuario = usuarioService.atualizarUsuario(id, usuarioAtualizado);
            logger.info("UsuarioController.atualizarUsuario(): Usuário atualizado - ID={}", usuario.getId());
            return ResponseEntity.ok(new UsuarioDTO(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getTelefone(),
                    usuario.getEndereco(),
                    usuario.getDataRegistro(),
                    usuario.getStatus()
            ));
        } catch (NotFoundException e) {
            logger.warn("UsuarioController.atualizarUsuario(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            logger.error("UsuarioController.atualizarUsuario(): Erro de validação: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("UsuarioController.atualizarUsuario(): Erro ao atualizar usuário: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        try {
            usuarioService.deletarUsuario(id);
            logger.info("UsuarioController.deletarUsuario(): Usuário deletado - ID={}", id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            logger.warn("UsuarioController.deletarUsuario(): {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            logger.error("UsuarioController.deletarUsuario(): Erro de regra de negócio: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("UsuarioController.deletarUsuario(): Erro ao deletar usuário: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}