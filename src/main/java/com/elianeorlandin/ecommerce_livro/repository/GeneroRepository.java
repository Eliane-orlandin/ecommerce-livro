package com.elianeorlandin.ecommerce_livro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.elianeorlandin.ecommerce_livro.model.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Long> {

	List<Genero> findAllByNomeContainingIgnoreCase(@Param("nome") String nome);
}
