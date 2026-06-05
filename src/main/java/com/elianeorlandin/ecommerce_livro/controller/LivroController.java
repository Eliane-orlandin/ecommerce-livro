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

import com.elianeorlandin.ecommerce_livro.model.Livro;
import com.elianeorlandin.ecommerce_livro.repository.GeneroRepository;
import com.elianeorlandin.ecommerce_livro.repository.LivroRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/livros")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LivroController {

	@Autowired
	private LivroRepository livroRepository;

	@Autowired
	private GeneroRepository generoRepository;

	@GetMapping
	public ResponseEntity<List<Livro>> getAll() {
		return ResponseEntity.ok(livroRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Livro> getById(@PathVariable Long id) {
		return livroRepository.findById(id).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Livro>> getByTitulo(@PathVariable String titulo) {
		return ResponseEntity.ok(livroRepository.findAllByTituloContainingIgnoreCase(titulo));
	}

	@GetMapping("/autor/{autor}")
	public ResponseEntity<List<Livro>> getByAutor(@PathVariable String autor) {
		return ResponseEntity.ok(livroRepository.findAllByAutorContainingIgnoreCase(autor));
	}

	@PostMapping
	public ResponseEntity<Livro> post(@Valid @RequestBody Livro livro) {
		if (generoRepository.existsById(livro.getGenero().getId())) {
			livro.setId(null);
			return ResponseEntity.status(HttpStatus.CREATED).body(livroRepository.save(livro));
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gênero não existe!", null);
	}

	@PutMapping
	public ResponseEntity<Livro> put(@Valid @RequestBody Livro livro) {
		if (livroRepository.existsById(livro.getId())) {
			if (generoRepository.existsById(livro.getGenero().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(livroRepository.save(livro));
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gênero não existe!", null);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT) 
	@DeleteMapping("/{id}") 
	public void delete(@PathVariable Long id) {
		Optional<Livro> postagem = livroRepository.findById(id);
		if (postagem.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		livroRepository.deleteById(id);
	}
	
	

}
