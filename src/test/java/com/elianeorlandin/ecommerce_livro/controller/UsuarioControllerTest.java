package com.elianeorlandin.ecommerce_livro.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.elianeorlandin.ecommerce_livro.model.Usuario;
import com.elianeorlandin.ecommerce_livro.model.UsuarioLogin;
import com.elianeorlandin.ecommerce_livro.repository.UsuarioRepository;
import com.elianeorlandin.ecommerce_livro.service.UsuarioService;
import com.elianeorlandin.ecommerce_livro.util.TestBuilder;

// Configura o ambiente de testes subindo o servidor em uma porta aleatória
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Permite usar o @BeforeAll em um método não estático
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// Executa os testes na ordem definida pela anotação @Order
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate; // Cliente HTTP para simular as requisições

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private static final String BASE_URL = "/usuarios";
	private static final String ADMIN = "root@root.com";
	private static final String SENHA = "rootroot";

	// Limpa o banco e cria um usuário administrador antes de todos os testes rodarem
	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();
		usuarioService.cadastrarUsuario(TestBuilder.criarUsuario(null, "Root", ADMIN, SENHA));
	}

	// Método auxiliar para obter o Token JWT realizando um login
	private String obterToken() {
		HttpEntity<UsuarioLogin> corpoRequisicao = new HttpEntity<UsuarioLogin>(
				TestBuilder.criarUsuarioLogin(ADMIN, SENHA));

		ResponseEntity<UsuarioLogin> corpoResposta = testRestTemplate.exchange(BASE_URL + "/logar", HttpMethod.POST,
				corpoRequisicao, UsuarioLogin.class);

		return corpoResposta.getBody().getToken(); // Ex: "Bearer eyJhb..."
	}

	@Test
	@Order(1)
	@DisplayName("Cadastrar Um Usuário")
	public void deveCadastrarUsuario() {
		// Usamos um Map em vez de Usuario para evitar que o Jackson oculte a senha (@JsonProperty WRITE_ONLY)
		Map<String, String> usuarioMap = new HashMap<>();
		usuarioMap.put("nome", "Paulo Antunes");
		usuarioMap.put("usuario", "paulo_antunes@email.com.br");
		usuarioMap.put("senha", "13465278");
		usuarioMap.put("foto", "-");

		HttpEntity<Map<String, String>> corpoRequisicao = new HttpEntity<>(usuarioMap);

		// Dispara a requisição POST para /usuarios/cadastrar
		ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange(BASE_URL + "/cadastrar", HttpMethod.POST,
				corpoRequisicao, Usuario.class);

		// Valida se o status retornou 201 CREATED e se os dados conferem
		assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
		assertEquals("Paulo Antunes", corpoResposta.getBody().getNome());
		assertEquals("paulo_antunes@email.com.br", corpoResposta.getBody().getUsuario());
	}

	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação de Usuário")
	public void naoDeveDuplicarUsuario() {
		// Cadastra a Maria pela primeira vez direto pelo service
		usuarioService.cadastrarUsuario(
				TestBuilder.criarUsuario(null, "Maria da Silva", "maria_silva@email.com.br", "13465278"));

		// Tenta cadastrar novamente pela rota do Controller
		Map<String, String> usuarioMap = new HashMap<>();
		usuarioMap.put("nome", "Maria da Silva");
		usuarioMap.put("usuario", "maria_silva@email.com.br");
		usuarioMap.put("senha", "13465278");
		usuarioMap.put("foto", "-");

		HttpEntity<Map<String, String>> corpoRequisicao = new HttpEntity<>(usuarioMap);

		ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange(BASE_URL + "/cadastrar", HttpMethod.POST,
				corpoRequisicao, Usuario.class);

		// Deve ser bloqueado pela validação de duplicidade (400)
		assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
	}

	@Test
	@Order(3)
	@DisplayName("Autenticar Usuário com sucesso")
	public void deveAutenticarUsuario() {
		// Monta o corpo com as credenciais cadastradas no @BeforeAll
		HttpEntity<UsuarioLogin> corpoRequisicao = new HttpEntity<UsuarioLogin>(
				TestBuilder.criarUsuarioLogin(ADMIN, SENHA));

		ResponseEntity<UsuarioLogin> corpoResposta = testRestTemplate.exchange(BASE_URL + "/logar", HttpMethod.POST,
				corpoRequisicao, UsuarioLogin.class);

		// Valida se o login foi aceito (200 OK)
		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
	}

	@Test
	@Order(4)
	@DisplayName("Autenticar Usuário com falha")
	public void deveFalharAoAutenticarComSenhaIncorreta() {
		// Envia uma senha errada intencionalmente
		HttpEntity<UsuarioLogin> corpoRequisicao = new HttpEntity<UsuarioLogin>(
				TestBuilder.criarUsuarioLogin(ADMIN, "senhaerrada"));

		ResponseEntity<UsuarioLogin> corpoResposta = testRestTemplate.exchange(BASE_URL + "/logar", HttpMethod.POST,
				corpoRequisicao, UsuarioLogin.class);

		// O Spring Security deve barrar (401 UNAUTHORIZED)
		assertEquals(HttpStatus.UNAUTHORIZED, corpoResposta.getStatusCode());
	}

	@Test
	@Order(5)
	@DisplayName("Atualizar um Usuário")
	public void deveAtualizarUsuario() {
		// Salva a Juliana inicialmente
		Usuario usuarioCadastrado = usuarioService.cadastrarUsuario(
				TestBuilder.criarUsuario(null, "Juliana Andrews", "juliana_andrews@email.com.br", "juliana123")).get();

		// Monta o objeto de atualização com um novo nome
		Map<String, Object> usuarioUpdate = new HashMap<>();
		usuarioUpdate.put("id", usuarioCadastrado.getId());
		usuarioUpdate.put("nome", "Juliana Andrews Ramos");
		usuarioUpdate.put("usuario", "juliana_ramos@email.com.br");
		usuarioUpdate.put("senha", "juliana123");
		usuarioUpdate.put("foto", "-");

		// Configura o cabeçalho Authorization com o token JWT
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", obterToken());

		HttpEntity<Map<String, Object>> corpoRequisicao = new HttpEntity<>(usuarioUpdate, headers);

		// Dispara o PUT
		ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange(BASE_URL + "/atualizar", HttpMethod.PUT,
				corpoRequisicao, Usuario.class);

		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		assertEquals("Juliana Andrews Ramos", corpoResposta.getBody().getNome());
		assertEquals("juliana_ramos@email.com.br", corpoResposta.getBody().getUsuario());
	}

	@Test
	@Order(6)
	@DisplayName("Mostrar todos os Usuários")
	public void deveMostrarTodosUsuarios() {
		usuarioService.cadastrarUsuario(
				TestBuilder.criarUsuario(null, "Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123"));

		usuarioService.cadastrarUsuario(
				TestBuilder.criarUsuario(null, "Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123"));

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", obterToken()); // Injeta o token na requisição

		HttpEntity<String> corpoRequisicao = new HttpEntity<>(null, headers);

		ResponseEntity<String> resposta = testRestTemplate.exchange(BASE_URL + "/all", HttpMethod.GET, corpoRequisicao,
				String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}

	@Test
	@Order(7)
	@DisplayName("Proteger acesso sem token")
	public void deveProtegerAcessoSemToken() {
		// Faz requisição para a listagem SEM enviar o cabeçalho Authorization
		ResponseEntity<String> resposta = testRestTemplate.exchange(BASE_URL + "/all", HttpMethod.GET, null,
				String.class);

		// O filtro JwtAuthFilter deve interceptar e barrar
		assertEquals(HttpStatus.UNAUTHORIZED, resposta.getStatusCode());
	}

	@Test
	@Order(8)
	@DisplayName("Buscar um Usuário por ID")
	public void deveBuscarUsuarioPorId() {
		Usuario usuarioCadastrado = usuarioService.cadastrarUsuario(
				TestBuilder.criarUsuario(null, "Marcos Paulo", "marcos_paulo@email.com.br", "marcos123")).get();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", obterToken());

		HttpEntity<String> corpoRequisicao = new HttpEntity<>(null, headers);

		ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange(BASE_URL + "/" + usuarioCadastrado.getId(),
				HttpMethod.GET, corpoRequisicao, Usuario.class);

		assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
		assertEquals("Marcos Paulo", corpoResposta.getBody().getNome());
	}

	@Test
	@Order(9)
	@DisplayName("Falhar ao buscar Usuário por ID inexistente")
	public void naoDeveEncontrarUsuarioPorId() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", obterToken());

		HttpEntity<String> corpoRequisicao = new HttpEntity<>(null, headers);

		// Consulta o ID 9999 que não está no banco H2
		ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange(BASE_URL + "/9999", HttpMethod.GET,
				corpoRequisicao, Usuario.class);

		assertEquals(HttpStatus.NOT_FOUND, corpoResposta.getStatusCode());
	}
}
