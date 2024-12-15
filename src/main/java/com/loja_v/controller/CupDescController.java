package com.loja_v.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.loja_v.model.CupDesc;
import com.loja_v.repository.CupDescRepository;

@RestController
public class CupDescController {
	
	@Autowired
	private CupDescRepository cupDescRepository;
	
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
