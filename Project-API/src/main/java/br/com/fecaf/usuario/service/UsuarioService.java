package br.com.fecaf.usuario.service;

import br.com.fecaf.usuario.model.Usuario;
import br.com.fecaf.usuario.repository.UsuarioRepository;
import br.com.fecaf.livro.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com ID " + id + " não encontrado."));
    }

    public Usuario salvarUsuario(Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("Já existe um usuário com o e-mail '" + usuario.getEmail() + "'.");
        }
        if (usuario.getDataRegistro() == null) {
            usuario.setDataRegistro(LocalDate.now());
        }
        if (usuario.getStatus() == null || usuario.getStatus().isEmpty()) {
            usuario.setStatus("Ativo");
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário com ID " + id + " não encontrado para atualização."));

        // Validação de e-mail duplicado ao atualizar
        if (!usuarioExistente.getEmail().equals(usuarioAtualizado.getEmail())) {
            Optional<Usuario> outroUsuarioComMesmoEmail = usuarioRepository.findByEmail(usuarioAtualizado.getEmail());
            if (outroUsuarioComMesmoEmail.isPresent() && !outroUsuarioComMesmoEmail.get().getId().equals(id)) {
                throw new IllegalArgumentException("Já existe outro usuário com o e-mail '" + usuarioAtualizado.getEmail() + "'.");
            }
        }

        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setTelefone(usuarioAtualizado.getTelefone());
        usuarioExistente.setEndereco(usuarioAtualizado.getEndereco());
        usuarioExistente.setStatus(usuarioAtualizado.getStatus());
        return usuarioRepository.save(usuarioExistente);
    }

    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("Usuário com ID " + id + " não encontrado para exclusão.");
        }
        usuarioRepository.deleteById(id);
    }
}