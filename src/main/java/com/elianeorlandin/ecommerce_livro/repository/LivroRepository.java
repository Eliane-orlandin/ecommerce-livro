package com.elianeorlandin.ecommerce_livro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.elianeorlandin.ecommerce_livro.model.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long>{

	List<Livro> findAllByTituloContainingIgnoreCase(@Param("titulo") String titulo);
}
