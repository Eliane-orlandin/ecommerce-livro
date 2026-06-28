package com.elianeorlandin.ecommerce_livro.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.elianeorlandin.ecommerce_livro.model.Genero;
import com.elianeorlandin.ecommerce_livro.repository.GeneroRepository;

@Controller
public class GeneroGraphQLController {

	@Autowired
    private GeneroRepository generoRepository;
    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public List<Genero> listarGeneros() {
        return generoRepository.findAll();
    }
    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public Genero buscarGeneroPorId(@Argument Long id) {
        return generoRepository.findById(id).orElse(null);
    }
    @PreAuthorize("isAuthenticated()")
    @MutationMapping
    public Genero salvarGenero(@Argument String nome, @Argument String descricao) {
        Genero genero = new Genero();
        genero.setNome(nome);
        genero.setDescricao(descricao); 
        return generoRepository.save(genero);
    }
    @PreAuthorize("isAuthenticated()")
    @MutationMapping
    public Boolean deletarGenero(@Argument Long id) {
        if (generoRepository.existsById(id)) {
            generoRepository.deleteById(id);
            return true;
        }
        return false;
    }
	
}
