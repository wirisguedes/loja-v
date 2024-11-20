package com.loja_v;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.loja_v.model.PessoaFisica;
import com.loja_v.model.PessoaJuridica;
import com.loja_v.repository.PessoaRepository;
import com.loja_v.service.PessoaUserService;

import junit.framework.TestCase;

@Profile("test")
@SpringBootTest(classes = LojaVApplication.class)
public class TestePessoaUsuario extends TestCase{
	
	@Autowired
	private PessoaUserService pessoaUserService;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Test
	public void testCadPessoaFisica() {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		
		pessoaJuridica.setCnpj("0275839872323");
		pessoaJuridica.setNome("Iris Guedes");
		pessoaJuridica.setEmail("test@test.com");
		pessoaJuridica.setTelefone("119842877");
		pessoaJuridica.setInscEstadual("465454545");
		pessoaJuridica.setInscMunicipal("4877878787");
		pessoaJuridica.setNomeFantasia("Abc");
		pessoaJuridica.setRazaoSocial("Abc");
		
		pessoaRepository.save(pessoaJuridica);
		
		/*
		PessoaFisica pessoaFisica = new PessoaFisica();
		
		pessoaFisica.setCpf("027583987");
		pessoaFisica.setNome("Iris Guedes");
		pessoaFisica.setEmail("test@test.com");
		pessoaFisica.setTelefone("119842877");
		pessoaFisica.setEmpresa(pessoaFisica);
		*/
		
	}

}
