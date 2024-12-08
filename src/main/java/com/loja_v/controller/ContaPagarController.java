package com.loja_v.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.loja_v.ExceptionLoja;
import com.loja_v.model.ContaPagar;
import com.loja_v.repository.ContaPagarRepository;

@RestController
public class ContaPagarController {
	
	@Autowired
	private ContaPagarRepository  contaPagarRepository;
	
	@ResponseBody 
	@PostMapping(value = "**/salvarContaPagar") 
	public ResponseEntity<ContaPagar> salvarContaPagar(@RequestBody ContaPagar contaPagar) throws ExceptionLoja { 
		
		if (contaPagar.getId() == null) {
		  List<ContaPagar> contaPagars = contaPagarRepository.buscarContaDesc(contaPagar.getDescricao().toUpperCase().trim());
		  
		  if (!contaPagars.isEmpty()) {
			  throw new ExceptionLoja("Já existe Conta a Pagar com a descrição: " + contaPagar.getDescricao());
		  }
		  
		  if (contaPagar.getPessoaFisica() == null || contaPagar.getPessoaFisica().getId() <= 0) {
			  throw new ExceptionLoja("A pessoa responsável pela conta deve ser informada");
		  }
		  
		  if (contaPagar.getEmpresa() == null || contaPagar.getEmpresa().getId() <= 0) {
			  throw new ExceptionLoja("A Empresa responsável pela conta deve ser informada");
		  }
		}
		
		
		ContaPagar acessoSalvo = contaPagarRepository.save(contaPagar);
		
		return new ResponseEntity<ContaPagar>(acessoSalvo, HttpStatus.OK);
	}
	
	
	
	@ResponseBody 
	@PostMapping(value = "**/deleteContaPagar")
	public ResponseEntity<String> deleteContaPagar(@RequestBody ContaPagar contaPagar) { 
		
		contaPagarRepository.deleteById(contaPagar.getId());
		
		return new ResponseEntity<String>("Conta Pagar Removida",HttpStatus.OK);
	}
	


	@ResponseBody
	@DeleteMapping(value = "**/deleteContaPagarPorId/{id}")
	public ResponseEntity<String> deleteContaPagarPorId(@PathVariable("id") Long id) { 
		
		contaPagarRepository.deleteById(id);
		
		return new ResponseEntity<String>("Conta Pagar Removida",HttpStatus.OK);
	}
	
	
	
	@ResponseBody
	@GetMapping(value = "**/obterContaPagar/{id}")
	public ResponseEntity<ContaPagar> obterContaPagar(@PathVariable("id") Long id) throws ExceptionLoja { 
		
		ContaPagar acesso = contaPagarRepository.findById(id).orElse(null);
		
		if (acesso == null) {
			throw new ExceptionLoja("Não encontrou Conta Pagar com código: " + id);
		}
		
		return new ResponseEntity<ContaPagar>(acesso,HttpStatus.OK);
	}
	
	
	
	@ResponseBody
	@GetMapping(value = "**/buscarPorContaDesc/{desc}")
	public ResponseEntity<List<ContaPagar>> buscarPorContaDesc(@PathVariable("desc") String desc) { 
		
		List<ContaPagar> acesso = contaPagarRepository.buscarContaDesc(desc.toUpperCase());
		
		return new ResponseEntity<List<ContaPagar>>(acesso,HttpStatus.OK);
	}


}
