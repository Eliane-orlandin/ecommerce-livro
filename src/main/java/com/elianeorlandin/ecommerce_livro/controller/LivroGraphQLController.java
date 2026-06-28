package com.elianeorlandin.ecommerce_livro.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.elianeorlandin.ecommerce_livro.model.Genero;
import com.elianeorlandin.ecommerce_livro.model.Livro;
import com.elianeorlandin.ecommerce_livro.repository.GeneroRepository;
import com.elianeorlandin.ecommerce_livro.repository.LivroRepository;

@Controller
public class LivroGraphQLController {
	
	@Autowired
    private LivroRepository livroRepository;
    
    @Autowired
    private GeneroRepository generoRepository;
    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public List<Livro> listarLivros() {
        return livroRepository.findAll();
    }
    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public Livro buscarLivroPorId(@Argument Long id) {
        return livroRepository.findById(id).orElse(null);
    }
    
    @PreAuthorize("isAuthenticated()")
    @MutationMapping
    public Livro salvarLivro(@Argument String titulo, @Argument String autor, @Argument Double preco, @Argument Long generoId) {
        Optional<Genero> genero = generoRepository.findById(generoId);
        if (genero.isPresent()) {
            Livro livro = new Livro();
            livro.setTitulo(titulo);
            livro.setAutor(autor);
            
            // Conversão de Double para BigDecimal
            if (preco != null) {
                livro.setPreco(BigDecimal.valueOf(preco));
            }
            
            livro.setGenero(genero.get());
            return livroRepository.save(livro);
        }
        throw new RuntimeException("Gênero não encontrado!");
    }
    
    @PreAuthorize("isAuthenticated()")
    @MutationMapping
    public Boolean deletarLivro(@Argument Long id) {
        if (livroRepository.existsById(id)) {
            livroRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
