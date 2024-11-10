package com.loja_v;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.loja_v.controller.AcessoController;
import com.loja_v.model.Acesso;
import com.loja_v.repository.AcessoRepository;
import com.loja_v.service.AcessoService;

@SpringBootTest(classes = LojaVApplication.class)
class LojaVApplicationTests {

	@Autowired
	private AcessoService acessoService;
	
	//@Autowired
	//private AcessoRepository acessoRepository;
	
	@Autowired
	private AcessoController acessoController;
	
	@Test
	public void testCadastraAcesso() {
		
		Acesso acesso = new Acesso();
		
		acesso.setDecricao("ROLE_ADMIN");
		
		acessoController.salvarAcesso(acesso);
	}

}
