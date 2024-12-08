package com.loja_v.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.loja_v.ExceptionLoja;
import com.loja_v.model.MarcaProduto;
import com.loja_v.repository.MarcaRepository;



@Controller
@RestController
public class MarcaProdutoController {
	
	
	@Autowired
	private MarcaRepository marcaRepository;
	
	@ResponseBody 
	@PostMapping(value = "**/salvarMarca") 
	public ResponseEntity<MarcaProduto> salvarMarca(@RequestBody @Valid MarcaProduto marcaProduto) throws ExceptionLoja { 
		
		if(marcaProduto.getId() == null) {
			List<MarcaProduto> marcaProdutos = marcaRepository.buscarMarcaDesc(marcaProduto.getNomeDesc().toUpperCase());
			if(!marcaProdutos.isEmpty()) {
				throw new ExceptionLoja("Já existe Marca com a descrição: " + marcaProduto.getNomeDesc());
			}
		}
		
		MarcaProduto marcaProdutoSalvo = marcaRepository.save(marcaProduto);
		return new ResponseEntity<MarcaProduto>(marcaProdutoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody 
	@PostMapping(value = "**/deletaMarca") 
	public ResponseEntity<?> deletaMarca(@RequestBody MarcaProduto marcaProduto) { 
		
		marcaRepository.deleteById(marcaProduto.getId());
		return new ResponseEntity("Marca produto removida", HttpStatus.OK);
	}
	
	
	//@Secured({"ROLE_GERENTE", "ROLE_ADMIN"})
	@ResponseBody 
	@DeleteMapping(value = "**/deletaMarcaPorId/{id}") 
	public ResponseEntity<?> deletaMarcaPorId(@PathVariable("id") Long id) { 
		
		marcaRepository.deleteById(id);
		return new ResponseEntity("Marca produto removida", HttpStatus.OK);
	}
	
	@ResponseBody 
	@GetMapping(value = "**/obterMarcaProdutoPorId/{id}") 
	public ResponseEntity<MarcaProduto> obterMarcaProdutoPorId(@PathVariable("id") Long id) throws ExceptionLoja { 
		
		MarcaProduto marcaProduto = marcaRepository.findById(id).orElse(null);
		
		if(marcaProduto == null) {
			throw new ExceptionLoja("Não encontrou Marca produto com código: " + id);
		}
		
		return new ResponseEntity<MarcaProduto>(marcaProduto, HttpStatus.OK);
	}
	
	@ResponseBody 
	@GetMapping(value = "**/buscarMarcaProdutoPorDesc/{desc}") 
	public ResponseEntity<List<MarcaProduto>> buscarMarcaProdutoPorDesc(@PathVariable("desc") String desc) { 
		
		List<MarcaProduto> marcaProduto = marcaRepository.buscarMarcaDesc(desc.toUpperCase());
		
		return new ResponseEntity<List<MarcaProduto>>(marcaProduto, HttpStatus.OK);
	}


}
