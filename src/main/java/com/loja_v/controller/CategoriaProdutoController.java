package com.loja_v.controller;

import java.util.List;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.loja_v.ExceptionLoja;
import com.loja_v.model.CategoriaProduto;
import com.loja_v.model.dto.CategoriaProdutoDTO;
import com.loja_v.repository.CategoriaProdutoRepository;

@RestController
public class CategoriaProdutoController {
	
	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository; 
	
	@ResponseBody
	@GetMapping(value = "**/listaPorPageCategoriaProduto/{idEmpresa}/{pagina}")
	public ResponseEntity<List<CategoriaProduto>> page(@PathVariable("idEmpresa") Long idEmpresa,
			@PathVariable("pagina") Integer pagina){
		
		Pageable pageable = PageRequest.of(pagina, 5, Sort.by("nomeDesc"));
		
		List<CategoriaProduto> lista = categoriaProdutoRepository.findPorPage(idEmpresa, pageable); 
		
		return new ResponseEntity<List<CategoriaProduto>>(lista, HttpStatus.OK);
	}
	
	@GetMapping(value = "**qtdPaginaCategoriaProduto/{idEmpresa}")
	public ResponseEntity<Integer> qtdPagina(@PathVariable("idEmpresa") Long idEmpresa){
		
		Integer qtdPagina = categoriaProdutoRepository.qtdPagina(idEmpresa);
		
		return new ResponseEntity<Integer>(qtdPagina, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarPorId/{id}")
	public ResponseEntity<CategoriaProduto> buscarPorDesc(@PathVariable("id") Long id) { 
		
		CategoriaProduto categoriaProduto = categoriaProdutoRepository.findById(id).get();
		
		return new ResponseEntity<CategoriaProduto>(categoriaProduto,HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarPorDescCatgoria/{desc}/{empresa}")
	public ResponseEntity<List<CategoriaProduto>> buscarPorDesc2(@PathVariable("desc") String desc,
			@PathVariable("empresa") Long empresa) { 
		
		List<CategoriaProduto> acesso = categoriaProdutoRepository.buscarCategoriaDes(desc.toUpperCase(), empresa);
		
		return new ResponseEntity<List<CategoriaProduto>>(acesso,HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/buscarPorDescCatgoria/{desc}")
	public ResponseEntity<List<CategoriaProduto>> buscarPorDesc(@PathVariable("desc") String desc) { 
		
		List<CategoriaProduto> acesso = categoriaProdutoRepository.buscarCategoriaDes(desc.toUpperCase());
		
		return new ResponseEntity<List<CategoriaProduto>>(acesso,HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/listarCategoriaProduto/{codEmpresa}")
	public ResponseEntity<List<CategoriaProduto>> listarCategoriaProduto(@PathVariable("codEmpresa") Long codEmpresa) { 
		
		List<CategoriaProduto> categoriaProduto = categoriaProdutoRepository.findAll(codEmpresa);
		
		return new ResponseEntity<List<CategoriaProduto>>(categoriaProduto,HttpStatus.OK);
	}


	@ResponseBody /*Poder dar um retorno da API*/
	@PostMapping(value = "**/deleteCategoria") /*Mapeando a url para receber JSON*/
	public ResponseEntity<?> deleteCategoria(@RequestBody CategoriaProduto categoriaProduto) throws ExceptionLoja { /*Recebe o JSON e converte pra Objeto*/
		
		if (categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent() == false) {
			throw new ExceptionLoja("Categoria já foi removida");
		}
		
		categoriaProdutoRepository.deleteById(categoriaProduto.getId());
		
		return new ResponseEntity<String>(new Gson().toJson("Categoria Removida"),HttpStatus.OK);
	}


	@ResponseBody
	@PostMapping(value = "**/salvarCategoria")
	public ResponseEntity<CategoriaProdutoDTO> salvarCategoria(@RequestBody CategoriaProduto categoriaProduto) throws  ExceptionLoja{
		
		if (categoriaProduto.getEmpresa() == null || (categoriaProduto.getEmpresa().getId() == null)) {
			throw new ExceptionLoja("A empresa deve ser informada.");
		}
		
		if (categoriaProduto.getId() == null && categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc())) {
			throw new ExceptionLoja("Não pode cadastar categoria com mesmo nome.");
		}
		
		
		CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);
		
		CategoriaProdutoDTO catgoriaProdutoDto = new CategoriaProdutoDTO();
		catgoriaProdutoDto.setId(categoriaSalva.getId());
		catgoriaProdutoDto.setNomeDesc(categoriaSalva.getNomeDesc());
		catgoriaProdutoDto.setEmpresa(categoriaSalva.getEmpresa().getId().toString());
		
		return new ResponseEntity<CategoriaProdutoDTO>(catgoriaProdutoDto, HttpStatus.OK);
	}



}
