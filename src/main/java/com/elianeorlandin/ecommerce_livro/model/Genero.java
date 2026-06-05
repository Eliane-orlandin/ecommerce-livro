package com.elianeorlandin.ecommerce_livro.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity 
@Table(name = "tb_generos")
public class Genero {

	// ---- Atributos ----
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 60, nullable = false)
	@NotBlank(message = "O atributo nome é obrigatório")
	@Size(min = 3, max = 60, message = "O nome deve ter entre 3 e 60 caracteres")
	private String nome;

	@Column(length = 500, nullable = false)
	@NotBlank(message = "A descrição é obrigatória")
	@Size(min = 10, max = 500, message = "A descrição deve ter entre 10 e 500 caracteres")
	private String descricao;

	// ---- Relacionamentos entre tabelas ----
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "genero", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties(value = "genero", allowSetters = true)
	private List<Livro> livro;

	// ---- Getters e Setters ----
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<Livro> getLivro() {
		return livro;
	}

	public void setLivro(List<Livro> livro) {
		this.livro = livro;
	}


	
	
	
	
}
