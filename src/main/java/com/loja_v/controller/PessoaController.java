package com.loja_v.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.loja_v.ExceptionLoja;
import com.loja_v.model.PessoaFisica;
import com.loja_v.model.PessoaJuridica;
import com.loja_v.repository.PessoaRepository;
import com.loja_v.service.PessoaUserService;
import com.loja_v.util.ValidaCNPJ;
import com.loja_v.util.ValidarCPF;

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
		
		if(pessoaJuridica.getId() == null && pessoaRepository.existeInscEstadualCadastrado(pessoaJuridica.getInscEstadual()) != null) {
			throw new ExceptionLoja("Já existe Inscrição Estadual cadastrado com o númeo: " + pessoaJuridica.getInscEstadual());
		}
		
		if(!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
			throw new ExceptionLoja("CNPJ: " + pessoaJuridica.getCnpj() + " inválido");
		}
		
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);
		
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value = "**/salvarPf")
	public ResponseEntity<PessoaFisica> salvarPf(@RequestBody PessoaFisica pessoaFisica) throws ExceptionLoja{
		
		if(pessoaFisica == null) {
			throw new ExceptionLoja("Pessoa Fisica não pode ser NULL");
		}
		
		if(pessoaFisica.getId() == null && pessoaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null) {
			throw new ExceptionLoja("Já existe CPF cadastrado com o númeo: " + pessoaFisica.getCpf());
		}
						
		if(!ValidarCPF.isCPF(pessoaFisica.getCpf())) {
			throw new ExceptionLoja("CPF: " + pessoaFisica.getCpf() + " inválido");
		}
		
		pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);
		
		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}

}
