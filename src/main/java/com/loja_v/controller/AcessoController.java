package com.loja_v.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.google.gson.Gson;
import com.loja_v.ExceptionLoja;
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
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) throws ExceptionLoja { //Recebe o JSON e converte para objeto
		
		if(acesso.getId() == null) {
			List<Acesso> acessos = acessoRepository.buscarAcessoDesc(acesso.getDescricao().toUpperCase());
			if(!acessos.isEmpty()) {
				throw new ExceptionLoja("Já existe acesso com a descrição: " + acesso.getDescricao());
			}
		}
		
		Acesso acessoSalvo = acessoService.save(acesso);
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody //Poder da um retorno da API
	@PostMapping(value = "**/deletaAcesso") // Mapeando a url para receber JSON
	public ResponseEntity<String> deletaAcesso(@RequestBody Acesso acesso) { //Recebe o JSON e converte para objeto
		
		acessoRepository.deleteById(acesso.getId());
		return new ResponseEntity<String>(new Gson().toJson("Acesso removido"), HttpStatus.OK);
	}
	
	
	//@Secured({"ROLE_GERENTE", "ROLE_ADMIN"})
	@ResponseBody 
	@DeleteMapping(value = "**/deletaAcessoPorId/{id}") 
	public ResponseEntity<?> deletaAcessoPorId(@PathVariable("id") Long id) { 
		
		acessoRepository.deleteById(id);
		return new ResponseEntity<String>(new Gson().toJson("Acesso Removido"), HttpStatus.OK);
	}
	
	@ResponseBody 
	@GetMapping(value = "**/obterAcessoPorId/{id}") 
	public ResponseEntity<Acesso> obterAcessoPorId(@PathVariable("id") Long id) throws ExceptionLoja { 
		
		Acesso acesso = acessoRepository.findById(id).orElse(null);
		
		if(acesso == null) {
			throw new ExceptionLoja("Não encontrou acesso com código: " + id);
		}
		
		return new ResponseEntity<Acesso>(acesso, HttpStatus.OK);
	}
	
	@ResponseBody 
	@GetMapping(value = "**/buscarPorDesc/{desc}") 
	public ResponseEntity<List<Acesso>> buscarPorDesc(@PathVariable("desc") String desc) { 
		
		List<Acesso> acesso = acessoRepository.buscarAcessoDesc(desc.toUpperCase());
		
		return new ResponseEntity<List<Acesso>>(acesso, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping(value = "**/listaPorPageAcesso/{idEmpresa}/{pagina}")
	public ResponseEntity<List<Acesso>> page(@PathVariable("idEmpresa") Long idEmpresa,
			@PathVariable("pagina") Integer pagina){
		
		Pageable pageable = PageRequest.of(pagina, 5, Sort.by("descricao"));
		
		List<Acesso> lista = acessoRepository.findPorPage(idEmpresa, pageable); 
		
		return new ResponseEntity<List<Acesso>>(lista, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/qtdPaginaAcesso/{idEmpresa}")
	public ResponseEntity<Integer> qtdPagina(@PathVariable("idEmpresa") Long idEmpresa){
		
		Integer qtdPagina = acessoRepository.qtdPagina(idEmpresa);
		
		return new ResponseEntity<Integer>(qtdPagina, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarPorAcesso/{desc}/{empresa}")
	public ResponseEntity<List<Acesso>> buscarPorAcesso(@PathVariable("desc") String desc,
			@PathVariable("empresa") Long empresa) { 
		
		List<Acesso> acesso = acessoRepository.buscarAcessoDes(desc.toUpperCase(), empresa);
		
		return new ResponseEntity<List<Acesso>>(acesso,HttpStatus.OK);
	}
}
