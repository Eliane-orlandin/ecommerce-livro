package com.elianeorlandin.ecommerce_livro.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.elianeorlandin.ecommerce_livro.model.Genero;
import com.elianeorlandin.ecommerce_livro.repository.GeneroRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/generos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class GeneroController {

	@Autowired
	private GeneroRepository generoRepository;

	@GetMapping
	public ResponseEntity<List<Genero>> getAll() {
		return ResponseEntity.ok(generoRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Genero> getById(@PathVariable Long id) {
		return generoRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/nome/{nome}") 
    public ResponseEntity<List<Genero>> getAllByNome(@PathVariable String nome){
        return ResponseEntity.ok(generoRepository.findAllByNomeContainingIgnoreCase(nome));
    }
	
	@PostMapping 
    public ResponseEntity<Genero> post(@Valid @RequestBody Genero genero){
    	genero.setId(null); 
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(generoRepository.save(genero));
    }
	
	@PutMapping 
    public ResponseEntity<Genero> put(@Valid @RequestBody Genero genero){
        return generoRepository.findById(genero.getId())
            .map(resposta -> ResponseEntity.status(HttpStatus.CREATED)
            .body(generoRepository.save(genero)))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
	
	@ResponseStatus(HttpStatus.NO_CONTENT) 
    @DeleteMapping("/{id}") 
    public void delete(@PathVariable Long id) {
        Optional<Genero> tema = generoRepository.findById(id);
        if(tema.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        generoRepository.deleteById(id);              
    }	
}
