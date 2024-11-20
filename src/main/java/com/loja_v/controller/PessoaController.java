package com.loja_v.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.loja_v.ExceptionLoja;
import com.loja_v.model.PessoaJuridica;
import com.loja_v.repository.PessoaRepository;
import com.loja_v.service.PessoaUserService;

@RestController
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaUserService pessoaUserService;
	
	@ResponseBody
	@PostMapping(value = "**/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody PessoaJuridica pessoaJuridica) throws ExceptionLoja{
		
		if(pessoaJuridica == null) {
			throw new ExceptionLoja("Pessoa Juridica não pode ser NULL");
		}
		
		if(pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null) {
			throw new ExceptionLoja("Já existe CNPJ cadastrado com o númeo: " + pessoaJuridica.getCnpj());
		}
		
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);
		
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}

}
