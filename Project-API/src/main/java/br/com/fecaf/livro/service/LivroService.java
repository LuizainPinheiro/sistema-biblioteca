package br.com.fecaf.livro.service;

import br.com.fecaf.autor.model.Autor;
import br.com.fecaf.categoria.model.Categoria;
import br.com.fecaf.livro.model.Livro;
import br.com.fecaf.livro.repository.LivroRepository;
import br.com.fecaf.autor.repository.AutorRepository;
import br.com.fecaf.categoria.repository.CategoriaRepository;
import br.com.fecaf.livro.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository; 

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Livro> listarLivros() {
        return livroRepository.findAll();
    }

    public Livro buscarLivroPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro com ID " + id + " não encontrado."));
    }

    @Transactional
    public Livro salvarLivro(Livro livro) {

        if (livro.getNumeroCopiasDisponiveis() == null) {
            livro.setNumeroCopiasDisponiveis(livro.getNumeroCopias());
        }


        if (livro.getCategoria() == null || livro.getCategoria().getId() == null) {
            throw new IllegalArgumentException("ID da Categoria é obrigatório.");
        }
        Categoria categoria = categoriaRepository.findById(livro.getCategoria().getId())
                .orElseThrow(() -> new NotFoundException("Categoria com ID " + livro.getCategoria().getId() + " não encontrada."));
        livro.setCategoria(categoria);


        if (livro.getAutores() != null && !livro.getAutores().isEmpty()) {

            livro.setAutores(livro.getAutores().stream()
                    .map(autorRequest -> {
                        if (autorRequest.getId() == null) {
                            throw new IllegalArgumentException("ID do Autor é obrigatório para autores existentes.");
                        }
                        return autorRepository.findById(autorRequest.getId())
                                .orElseThrow(() -> new NotFoundException("Autor com ID " + autorRequest.getId() + " não encontrado."));
                    })
                    .collect(Collectors.toSet()));
        } else {
            livro.setAutores(new HashSet<>());
        }

        return livroRepository.save(livro);
    }


    @Transactional
    public Livro atualizarLivro(Long id, Livro livroAtualizado) {
        Livro livroExistente = livroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Livro com ID " + id + " não encontrado para atualização."));

        livroExistente.setTitulo(livroAtualizado.getTitulo());
        livroExistente.setIsbn(livroAtualizado.getIsbn());
        livroExistente.setEditora(livroAtualizado.getEditora());
        livroExistente.setAnoPublicacao(livroAtualizado.getAnoPublicacao());
        livroExistente.setGenero(livroAtualizado.getGenero());
        livroExistente.setNumeroCopias(livroAtualizado.getNumeroCopias());
        livroExistente.setNumeroCopiasDisponiveis(livroAtualizado.getNumeroCopiasDisponiveis());

        // Atualizar Categoria
        if (livroAtualizado.getCategoria() != null && livroAtualizado.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(livroAtualizado.getCategoria().getId())
                    .orElseThrow(() -> new NotFoundException("Categoria com ID " + livroAtualizado.getCategoria().getId() + " não encontrada."));
            livroExistente.setCategoria(categoria);
        } else {
            throw new IllegalArgumentException("ID da Categoria é obrigatório para atualização.");
        }

        // Atualizar Autores
        if (livroAtualizado.getAutores() != null) {
            livroExistente.getAutores().clear();
            livroAtualizado.getAutores().forEach(autorRequest -> {
                if (autorRequest.getId() == null) {
                    throw new IllegalArgumentException("ID do Autor é obrigatório para autores existentes na atualização.");
                }
                Autor autor = autorRepository.findById(autorRequest.getId())
                        .orElseThrow(() -> new NotFoundException("Autor com ID " + autorRequest.getId() + " não encontrado."));
                livroExistente.addAutor(autor); // Reassocia
            });
        } else {
            livroExistente.getAutores().clear();
        }

        return livroRepository.save(livroExistente);
    }

    public void deletarLivro(Long id) {
        if (!livroRepository.existsById(id)) {
            throw new NotFoundException("Livro com ID " + id + " não encontrado para exclusão.");
        }
        livroRepository.deleteById(id);
    }
}