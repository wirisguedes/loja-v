package com.loja_v.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loja_v.model.Acesso;
import com.loja_v.repository.AcessoRepository;

@Service
public class AcessoService {

	@Autowired
	private AcessoRepository acessoRepository;
	
	public Acesso save(Acesso acesso) {
		
		//possivel realizar qualquer tipo de validação antes de salvar
		return acessoRepository.save(acesso);
	}
	
	
}
