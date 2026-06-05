package com.elianeorlandin.ecommerce_livro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elianeorlandin.ecommerce_livro.model.Livro;

public interface LivroRepository extends JpaRepository<Livro, Long>{

	List<Livro> findAllByTituloContainingIgnoreCase(String titulo);
	List<Livro> findAllByAutorContainingIgnoreCase(String autor);

}
