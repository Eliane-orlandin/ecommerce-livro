package com.elianeorlandin.ecommerce_livro.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_livros")
public class Livro {
    
	// ---- Atributos ----
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, nullable = false)
	@NotBlank(message = "O atributo título é obrigatório")
	@Size(min = 5, max = 100, message = "O atributo título deve ter entre 5 e 100 caracteres")
	private String titulo;

	@Column(length = 60, nullable = false)
	@NotBlank(message = "O atributo autor é obrigatório")
	@Size(min = 3, max = 60, message = "O autor deve ter entre 3 e 60 caracteres")
	private String autor;

	@Column(length = 500, nullable = false)
	@NotBlank(message = "A descrição é obrigatória")
	@Size(min = 10, max = 500, message = "A descrição deve ter entre 10 e 500 caracteres")
	private String descricao;

	@Column(nullable = false)
	@NotNull(message = "O preço é obrigatório")
	@Positive(message = "O preço deve ser maior que zero")
	@Digits(integer = 6, fraction = 2, message = "O preço deve ter no máximo 6 dígitos inteiros e 2 casas decimais")
	private BigDecimal preco;
	
	@Column(length = 255)
	@Size(max = 255, message = "A URL da capa não pode ser maior do que 255 caracteres")
	private String capa;
	
	// ---- Preenche e atualiza a data/hora automaticamente a cada modificação ----
	@UpdateTimestamp 
	private LocalDateTime data;

	// ---- Relacionamentos entre tabelas
	@ManyToOne
	@JsonIgnoreProperties("livro")
	private Usuario usuario;
	
	@ManyToOne
	@JsonIgnoreProperties("livro")
	private Genero genero;
	
	// ---- Getters e Setters ----
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public String getCapa() {
		return capa;
	}

	public void setCapa(String capa) {
		this.capa = capa;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}
	
	
	

}
