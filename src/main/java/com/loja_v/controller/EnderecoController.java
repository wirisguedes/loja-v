package com.loja_v.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.loja_v.model.Endereco;
import com.loja_v.repository.EnderecoRepository;

@Controller
@RestController
public class EnderecoController {

	@Autowired
	private EnderecoRepository enderecoRepository;

	@ResponseBody
	@PostMapping(value = "**/deleteEndereco")
	public ResponseEntity<String> deleteEndereco(@RequestBody Endereco endereco) {

		if (endereco.getId() != null) {
			enderecoRepository.deleteById(endereco.getId());
		}
		return new ResponseEntity<String>(new Gson().toJson("Endereco Removido"), HttpStatus.OK);
	}

}
