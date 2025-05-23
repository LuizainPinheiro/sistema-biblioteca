package br.com.fecaf.categoria.service;

import br.com.fecaf.categoria.model.Categoria;
import br.com.fecaf.categoria.repository.CategoriaRepository;
import br.com.fecaf.livro.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarCategoriaPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria com ID " + id + " não encontrada."));
    }

    public Categoria salvarCategoria(Categoria categoria) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findByNome(categoria.getNome());
        if (categoriaExistente.isPresent()) {
            throw new IllegalArgumentException("Já existe uma categoria com o nome '" + categoria.getNome() + "'.");
        }
        return categoriaRepository.save(categoria);
    }

    public Categoria atualizarCategoria(Long id, Categoria categoriaAtualizada) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria com ID " + id + " não encontrada para atualização."));

        if (!categoriaExistente.getNome().equals(categoriaAtualizada.getNome())) {
            Optional<Categoria> outraCategoriaComMesmoNome = categoriaRepository.findByNome(categoriaAtualizada.getNome());
            if (outraCategoriaComMesmoNome.isPresent() && !outraCategoriaComMesmoNome.get().getId().equals(id)) {
                throw new IllegalArgumentException("Já existe outra categoria com o nome '" + categoriaAtualizada.getNome() + "'.");
            }
        }
        categoriaExistente.setNome(categoriaAtualizada.getNome());
        return categoriaRepository.save(categoriaExistente);
    }

    public void deletarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria com ID " + id + " não encontrada para exclusão."));

        if (!categoria.getLivros().isEmpty()) {
            throw new IllegalArgumentException("Não é possível deletar a categoria, pois ela possui livros associados. Desassocie os livros primeiro ou mude o relacionamento.");
        }
        categoriaRepository.delete(categoria);
    }
}