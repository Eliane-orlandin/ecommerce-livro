package com.elianeorlandin.ecommerce_livro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elianeorlandin.ecommerce_livro.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	
	Optional<Usuario> findByUsuario(String usuario);
}
