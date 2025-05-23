package br.com.fecaf.autor.service;

import br.com.fecaf.autor.model.Autor;
import br.com.fecaf.autor.repository.AutorRepository;
import br.com.fecaf.livro.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    public Autor buscarAutorPorId(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Autor com ID " + id + " não encontrado."));
    }

    public Autor salvarAutor(Autor autor) {
        Optional<Autor> autorExistente = autorRepository.findByNome(autor.getNome());
        if (autorExistente.isPresent()) {
            throw new IllegalArgumentException("Já existe um autor com o nome '" + autor.getNome() + "'.");
        }
        return autorRepository.save(autor);
    }

    public Autor atualizarAutor(Long id, Autor autorAtualizado) {
        Autor autorExistente = autorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Autor com ID " + id + " não encontrado para atualização."));

        if (!autorExistente.getNome().equals(autorAtualizado.getNome())) {
            Optional<Autor> outroAutorComMesmoNome = autorRepository.findByNome(autorAtualizado.getNome());
            if (outroAutorComMesmoNome.isPresent() && !outroAutorComMesmoNome.get().getId().equals(id)) {
                throw new IllegalArgumentException("Já existe outro autor com o nome '" + autorAtualizado.getNome() + "'.");
            }
        }
        autorExistente.setNome(autorAtualizado.getNome());
        autorExistente.setBiografia(autorAtualizado.getBiografia());
        return autorRepository.save(autorExistente);
    }

    public void deletarAutor(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Autor com ID " + id + " não encontrado para exclusão."));

        if (!autor.getLivros().isEmpty()) {
            throw new IllegalArgumentException("Não é possível deletar o autor, pois ele possui livros associados. Desassocie os livros primeiro.");
        }
        autorRepository.delete(autor);
    }
}