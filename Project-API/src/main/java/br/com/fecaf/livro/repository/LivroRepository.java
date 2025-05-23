package br.com.fecaf.livro.repository;

import br.com.fecaf.livro.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    // Métodos personalizados, se houver
}