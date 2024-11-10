package com.loja_v;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loja_v.controller.AcessoController;
import com.loja_v.model.Acesso;
import com.loja_v.repository.AcessoRepository;
import com.loja_v.service.AcessoService;

import junit.framework.TestCase;

@SpringBootTest(classes = LojaVApplication.class)
class LojaVApplicationTests extends TestCase{

	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@Autowired
	private AcessoController acessoController;
	
	@Autowired
	private WebApplicationContext wac;
	
	/*Test end-point salvar*/
	@Test
	public void testRestApiCadastroAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_COMPRADOR");
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
									.perform(MockMvcRequestBuilders.post("/salvarAcesso")
									.content(objectMapper.writeValueAsString(acesso))
									.accept(MediaType.APPLICATION_JSON)
									.contentType(MediaType.APPLICATION_JSON));
		
		Acesso objetoRetorno = objectMapper.
								readValue(retornoApi.andReturn().getResponse().getContentAsString(),
								Acesso.class);
		
		assertEquals(acesso.getDescricao(), objetoRetorno.getDescricao());
	}
	
	/*Test end-point delete*/
	@Test
	public void testRestApiDeleteAcesso() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_TESTE_DELETE");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
									.perform(MockMvcRequestBuilders.post("/deletaAcesso")
									.content(objectMapper.writeValueAsString(acesso))
									.accept(MediaType.APPLICATION_JSON)
									.contentType(MediaType.APPLICATION_JSON));
		
		System.out.println("Retorno API: " + retornoApi.andReturn().getResponse().getContentAsString());
		
		assertEquals("Acesso removido", retornoApi.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	
	}
	
	@Test
	public void testRestApiDeleteAcessoPorId() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_TESTE_DELETE_ID");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
									.perform(MockMvcRequestBuilders.delete("/deletaAcessoPorId/" + acesso.getId())
									.content(objectMapper.writeValueAsString(acesso))
									.accept(MediaType.APPLICATION_JSON)
									.contentType(MediaType.APPLICATION_JSON));
		
		System.out.println("Retorno API: " + retornoApi.andReturn().getResponse().getContentAsString());
		
		assertEquals("Acesso removido", retornoApi.andReturn().getResponse().getContentAsString());
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
	
	}
	
	@Test
	public void testRestApiObterAcessoId() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_OBTER_ID");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
									.perform(MockMvcRequestBuilders.get("/obterAcessoPorId/" + acesso.getId())
									.content(objectMapper.writeValueAsString(acesso))
									.accept(MediaType.APPLICATION_JSON)
									.contentType(MediaType.APPLICATION_JSON));
		
	
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
		Acesso acessoRetorno = objectMapper.readValue(retornoApi.andReturn().getResponse().getContentAsString(), Acesso.class);
		
		assertEquals(acesso.getDescricao(), acessoRetorno.getDescricao());
	}
	
	@Test
	public void testRestApiObterAcessoDesc() throws JsonProcessingException, Exception {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		MockMvc mockMvc = builder.build();
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_TESTE_OBTER_LIST");
		
		acesso = acessoRepository.save(acesso);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		ResultActions retornoApi = mockMvc
									.perform(MockMvcRequestBuilders.get("/buscarPorDesc/OBTER_LIST")
									.content(objectMapper.writeValueAsString(acesso))
									.accept(MediaType.APPLICATION_JSON)
									.contentType(MediaType.APPLICATION_JSON));
		
	
		assertEquals(200, retornoApi.andReturn().getResponse().getStatus());
		
		List<Acesso> retornoApiList = objectMapper.
										readValue(retornoApi.andReturn()
												.getResponse().getContentAsString(),
												new TypeReference<List<Acesso>>() {});
		
		assertEquals(1, retornoApiList.size());
		
		assertEquals(acesso.getDescricao(), retornoApiList.get(0).getDescricao());
		
		acessoRepository.deleteById(acesso.getId());
		
	}
	
	@Test
	public void testCadastraAcesso() {
		
		Acesso acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ADMIN");
		
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		assertEquals(true, acesso.getId() > 0);
		
		assertEquals("ROLE_ADMIN", acesso.getDescricao());
		
		/*Test de carregamento*/
		Acesso acesso2 = acessoRepository.findById(acesso.getId()).get();
		
		assertEquals(acesso.getId(), acesso2.getId());
		
		/*Teste delete*/
		
		acessoRepository.deleteById(acesso2.getId());
		
		acessoRepository.flush(); /*Roda SQL de delete no banco de dados*/
		
		Acesso acesso3 = acessoRepository.findById(acesso2.getId()).orElse(null);
		
		assertEquals(true, acesso3 == null);
		
		/*Teste de query*/
		
		acesso = new Acesso();
		
		acesso.setDescricao("ROLE_ALUNO");
		
		acesso = acessoController.salvarAcesso(acesso).getBody();
		
		List<Acesso> acessos = acessoRepository.buscarAcessoDesc("ALUNO".trim().toUpperCase());
		
		assertEquals(1, acessos.size());
		
		acessoRepository.deleteById(acesso.getId());
		
		

		
	}

}
