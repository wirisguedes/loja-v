package com.loja_v.controller;

import java.util.List;

import javax.validation.Valid;

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
import com.loja_v.model.CupDesc;
import com.loja_v.model.MarcaProduto;
import com.loja_v.repository.CupDescRepository;

@RestController
public class CupDescController {
	
	@Autowired
	private CupDescRepository cupDescRepository;
	
	@ResponseBody 
	@DeleteMapping(value = "**/deletaCupPorId/{id}") 
	public ResponseEntity<?> deletaCupPorId(@PathVariable("id") Long id) { 
		
		cupDescRepository.deleteById(id);
		return new ResponseEntity("Cupom produto removido", HttpStatus.OK);
	}
	
	@ResponseBody 
	@GetMapping(value = "**/obterCupomPorId/{id}") 
	public ResponseEntity<CupDesc> obterCupomPorId(@PathVariable("id") Long id) throws ExceptionLoja { 
		
		CupDesc cupDsc = cupDescRepository.findById(id).orElse(null);
		
		if(cupDsc == null) {
			throw new ExceptionLoja("Não encontrou Cupom produto com código: " + id);
		}
		
		return new ResponseEntity<CupDesc>(cupDsc, HttpStatus.OK);
	}
	
	@ResponseBody 
	@PostMapping(value = "**/salvarCupDesc") 
	public ResponseEntity<CupDesc> salvarCupDesc(@RequestBody @Valid CupDesc cupDesc) throws ExceptionLoja { 
		
		CupDesc cupDesc2 = cupDescRepository.save(cupDesc);
		
		return new ResponseEntity<CupDesc>(cupDesc2, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "**/listaCupomDesc/{idEmpresa}")
	public ResponseEntity<List<CupDesc>> listaCupomDesc(@PathVariable("idEmpresa") Long idEmpresa){
		
		return new ResponseEntity<List<CupDesc>>(cupDescRepository.cupDescontoPorEmpresa(idEmpresa), HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/listaCupomDesc")
	public ResponseEntity<List<CupDesc>> listaCupomDesc(){
		
		return new ResponseEntity<List<CupDesc>>(cupDescRepository.findAll() , HttpStatus.OK);
	}


}
