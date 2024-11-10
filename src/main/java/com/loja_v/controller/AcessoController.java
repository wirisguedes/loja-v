package com.loja_v.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.loja_v.model.Acesso;
import com.loja_v.repository.AcessoRepository;
import com.loja_v.service.AcessoService;

@Controller
@RestController
public class AcessoController {
	
	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	@ResponseBody //Poder da um retorno da API
	@PostMapping(value = "**/salvarAcesso") // Mapeando a url para receber JSON
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) { //Recebe o JSON e converte para objeto
		
		Acesso acessoSalvo = acessoService.save(acesso);
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody //Poder da um retorno da API
	@PostMapping(value = "**/deletaAcesso") // Mapeando a url para receber JSON
	public ResponseEntity<?> deletaAcesso(@RequestBody Acesso acesso) { //Recebe o JSON e converte para objeto
		
		acessoRepository.deleteById(acesso.getId());
		return new ResponseEntity("Acesso removido", HttpStatus.OK);
	}
	
	@ResponseBody 
	@DeleteMapping(value = "**/deletaAcessoPorId/{id}") 
	public ResponseEntity<?> deletaAcessoPorId(@PathVariable("id") Long id) { 
		
		acessoRepository.deleteById(id);
		return new ResponseEntity("Acesso removido", HttpStatus.OK);
	}
	
	@ResponseBody 
	@GetMapping(value = "**/obterAcessoPorId/{id}") 
	public ResponseEntity<Acesso> obterAcessoPorId(@PathVariable("id") Long id) { 
		
		Acesso acesso = acessoRepository.findById(id).get();
		return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);
	}
	
	@ResponseBody 
	@GetMapping(value = "**/buscarPorDesc/{desc}") 
	public ResponseEntity<List<Acesso>> buscarPorDesc(@PathVariable("desc") String desc) { 
		
		List<Acesso> acesso = acessoRepository.buscarAcessoDesc(desc);
		
		return new ResponseEntity<List<Acesso>>(acesso, HttpStatus.OK);
	}


}
