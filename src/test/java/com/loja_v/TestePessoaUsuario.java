package com.loja_v;

import java.util.Calendar;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;


import com.loja_v.controller.PessoaController;
import com.loja_v.enums.TipoEndereco;
import com.loja_v.model.Endereco;
import com.loja_v.model.PessoaFisica;
import com.loja_v.model.PessoaJuridica;
import com.loja_v.repository.PessoaFisicaRepository;
import com.loja_v.repository.PessoaRepository;
import com.loja_v.service.PessoaUserService;

import junit.framework.TestCase;
import net.datafaker.Faker;

@Profile("test")
@SpringBootTest(classes = LojaVApplication.class)
public class TestePessoaUsuario extends TestCase {

	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	@Test
	public void testCadPessoaJuridica() throws ExceptionLoja {

		Faker faker = new Faker(new Locale("pt-BR"));

		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome(faker.name().firstName());
		pessoaJuridica.setEmail("wirisguedes@gmail.com"); 
		pessoaJuridica.setTelefone("45999795800");
		pessoaJuridica.setInscEstadual("65556565656665");
		pessoaJuridica.setInscMunicipal("55554565656565");
		pessoaJuridica.setNomeFantasia("54556565665");
		pessoaJuridica.setRazaoSocial("4656656566");
		
		Endereco endereco1 = new Endereco();
		endereco1.setBairro("Jd Dias");
		endereco1.setCep("556556565");
		endereco1.setComplemento("Casa cinza");
		endereco1.setEmpresa(pessoaJuridica);
		endereco1.setNumero("389");
		endereco1.setPessoa(pessoaJuridica);
		endereco1.setRuaLogra("Av. são joao sexto");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("PR");
		endereco1.setCidade("Curitiba");
		
		
		Endereco endereco2 = new Endereco();
		endereco2.setBairro("Jd Maracana");
		endereco2.setCep("7878778");
		endereco2.setComplemento("Andar 4");
		endereco2.setEmpresa(pessoaJuridica);
		endereco2.setNumero("555");
		endereco2.setPessoa(pessoaJuridica);
		endereco2.setRuaLogra("Av. maringá");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("TO");
		endereco2.setCidade("Curitiba");
		
		pessoaJuridica.getEnderecos().add(endereco2);
		pessoaJuridica.getEnderecos().add(endereco1);


		pessoaJuridica = pessoaController.salvarPj(pessoaJuridica).getBody();
		
		assertEquals(true, pessoaJuridica.getId() > 0 );
		
		for (Endereco endereco : pessoaJuridica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}
		
		assertEquals(2, pessoaJuridica.getEnderecos().size());


	}

	
	@Test
	public void testCadPessoaFisica() throws ExceptionLoja {

		Faker faker = new Faker(new Locale("pt-BR"));

		PessoaJuridica pessoaJuridica = pessoaRepository.existeCnpjCadastrado("1732386295161");
		PessoaFisica pessoaFisica = new PessoaFisica();
		
		pessoaFisica.setCpf(faker.cpf().valid());
		pessoaFisica.setNome(faker.name().firstName());
		pessoaFisica.setEmail("wirisguedes4343434@gmail.com"); 
		pessoaFisica.setTelefone("45999795800");
		pessoaFisica.setEmpresa(pessoaJuridica);
				
		Endereco endereco1 = new Endereco();
		endereco1.setBairro("Jd Dias");
		endereco1.setCep("556556565");
		endereco1.setComplemento("Casa cinza");
		endereco1.setEmpresa(pessoaFisica);
		endereco1.setNumero("389");
		endereco1.setPessoa(pessoaFisica);
		endereco1.setRuaLogra("Av. são joao sexto");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("PR");
		endereco1.setCidade("Curitiba");
		endereco1.setEmpresa(pessoaJuridica);
		
		
		Endereco endereco2 = new Endereco();
		endereco2.setBairro("Jd Maracana");
		endereco2.setCep("7878778");
		endereco2.setComplemento("Andar 4");
	
		endereco2.setNumero("555");
		endereco2.setPessoa(pessoaFisica);
		endereco2.setRuaLogra("Av. maringá");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("TO");
		endereco2.setCidade("Curitiba");
		
		pessoaFisica.getEnderecos().add(endereco2);
		pessoaFisica.getEnderecos().add(endereco1);
		endereco2.setEmpresa(pessoaJuridica);


		pessoaFisica = pessoaController.salvarPf(pessoaFisica).getBody();
		
		assertEquals(true, pessoaFisica.getId() > 0 );
		
		for (Endereco endereco : pessoaFisica.getEnderecos()) {
			assertEquals(true, endereco.getId() > 0);
		}
		
		assertEquals(2, pessoaFisica.getEnderecos().size());


	}
}
