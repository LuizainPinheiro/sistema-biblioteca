package br.com.fecaf.emprestimo.service;

import br.com.fecaf.emprestimo.model.Emprestimo;
import br.com.fecaf.emprestimo.repository.EmprestimoRepository;
import br.com.fecaf.livro.model.Livro;
import br.com.fecaf.livro.repository.LivroRepository;
import br.com.fecaf.usuario.model.Usuario;
import br.com.fecaf.usuario.repository.UsuarioRepository;
import br.com.fecaf.livro.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;
    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Emprestimo> listarEmprestimos() {
        return emprestimoRepository.findAll();
    }

    public Emprestimo buscarEmprestimoPorId(Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empréstimo com ID " + id + " não encontrado."));
    }

    @Transactional
    public Emprestimo realizarEmprestimo(Long livroId, Long usuarioId, LocalDate dataDevolucaoPrevista) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new NotFoundException("Livro com ID " + livroId + " não encontrado."));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário com ID " + usuarioId + " não encontrado."));

        if (livro.getNumeroCopiasDisponiveis() <= 0) {
            throw new IllegalArgumentException("Não há cópias disponíveis do livro '" + livro.getTitulo() + "' para empréstimo.");
        }

        // Diminui o número de cópias disponíveis
        livro.setNumeroCopiasDisponiveis(livro.getNumeroCopiasDisponiveis() - 1);
        livroRepository.save(livro);

        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setUsuario(usuario);
        emprestimo.setDataEmprestimo(LocalDateTime.now());
        emprestimo.setDataDevolucaoPrevista(dataDevolucaoPrevista);
        emprestimo.setDataDevolucaoReal(null);

        return emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public Emprestimo registrarDevolucao(Long emprestimoId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new NotFoundException("Empréstimo com ID " + emprestimoId + " não encontrado."));

        if (emprestimo.getDataDevolucaoReal() != null) {
            throw new IllegalArgumentException("Este empréstimo já foi devolvido.");
        }

        emprestimo.setDataDevolucaoReal(LocalDate.now());


        Livro livro = emprestimo.getLivro();
        livro.setNumeroCopiasDisponiveis(livro.getNumeroCopiasDisponiveis() + 1);
        livroRepository.save(livro);

        return emprestimoRepository.save(emprestimo);
    }

    public void deletarEmprestimo(Long id) {

        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Empréstimo com ID " + id + " não encontrado para exclusão."));

        if (emprestimo.getDataDevolucaoReal() == null) {
            throw new IllegalArgumentException("Não é possível deletar um empréstimo ativo. Registre a devolução primeiro.");
        }
        emprestimoRepository.deleteById(id);
    }
}