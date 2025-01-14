package com.loja_v.controller;

import java.util.ArrayList;
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
import com.loja_v.model.NotaFiscalCompra;
import com.loja_v.model.NotaFiscalVenda;
import com.loja_v.model.dto.ObejtoRequisicaoRelatorioProdCompraNotaFiscalDTO;
import com.loja_v.model.dto.ObejtoRequisicaoRelatorioProdutoAlertaEstoqueDTO;
import com.loja_v.model.dto.ObjetoRelatorioStatusCompraDTO;
import com.loja_v.repository.NotaFiscalCompraRepository;
import com.loja_v.repository.NotaFiscalVendaRepository;
import com.loja_v.service.NotaFiscalCompraService;

@RestController
public class NotaFiscalCompraController {
	
	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;
	
	@Autowired
	private NotaFiscalCompraRepository notaFiscalCompraRepository;
	
	@Autowired
	private NotaFiscalCompraService notaFiscalCompraService;
	

	@ResponseBody
	@PostMapping(value = "**/relatorioStatusCompra")
	public ResponseEntity<List<ObjetoRelatorioStatusCompraDTO>> relatorioStatusCompra(
			@Valid @RequestBody ObjetoRelatorioStatusCompraDTO objetoRelatorioStatusCompra) {

		List<ObjetoRelatorioStatusCompraDTO> retorno = new ArrayList<ObjetoRelatorioStatusCompraDTO>();

		retorno = notaFiscalCompraService.relatorioStatusVendaLoja(objetoRelatorioStatusCompra);

		return new ResponseEntity<List<ObjetoRelatorioStatusCompraDTO>>(retorno, HttpStatus.OK);

	}
	
	@ResponseBody
	@PostMapping(value = "**/relatorioProdCompradoNotaFiscal")
	public ResponseEntity<List<ObejtoRequisicaoRelatorioProdCompraNotaFiscalDTO>> relatorioProdCompradoNotaFiscal
	    (@Valid @RequestBody ObejtoRequisicaoRelatorioProdCompraNotaFiscalDTO obejtoRequisicaoRelatorioProdCompraNotaFiscalDto){
		
		List<ObejtoRequisicaoRelatorioProdCompraNotaFiscalDTO> retorno = 
				new ArrayList<ObejtoRequisicaoRelatorioProdCompraNotaFiscalDTO>();
		
		retorno = notaFiscalCompraService.gerarRelatorioProdCompraNota(obejtoRequisicaoRelatorioProdCompraNotaFiscalDto);
		
		
		return new ResponseEntity<List<ObejtoRequisicaoRelatorioProdCompraNotaFiscalDTO>>(retorno, HttpStatus.OK);
		
	}

	@ResponseBody
	@PostMapping(value = "**/relatorioProdAlertaEstoque")
	public ResponseEntity<List<ObejtoRequisicaoRelatorioProdutoAlertaEstoqueDTO>> relatorioProdAlertaEstoque(
			@Valid @RequestBody ObejtoRequisicaoRelatorioProdutoAlertaEstoqueDTO obejtoRequisicaoRelatorioProdCompraNotaFiscalDto) {

		List<ObejtoRequisicaoRelatorioProdutoAlertaEstoqueDTO> retorno = new ArrayList<ObejtoRequisicaoRelatorioProdutoAlertaEstoqueDTO>();

		retorno = notaFiscalCompraService.gerarRelatorioAlertaEstoque(obejtoRequisicaoRelatorioProdCompraNotaFiscalDto);

		return new ResponseEntity<List<ObejtoRequisicaoRelatorioProdutoAlertaEstoqueDTO>>(retorno, HttpStatus.OK);

	}

	@ResponseBody 
	@PostMapping(value = "**/salvarNotaFiscalCompra")
	public ResponseEntity<NotaFiscalCompra> salvarNotaFiscalCompra(@RequestBody @Valid NotaFiscalCompra notaFiscalCompra) throws ExceptionLoja {
		
		if (notaFiscalCompra.getId() == null) {
		  
			if (notaFiscalCompra.getDescricaoObs() != null) {
				boolean existe = notaFiscalCompraRepository.existeNotaComDescricao(notaFiscalCompra.getDescricaoObs().toUpperCase().trim());
			   
				if(existe) {
				   throw new ExceptionLoja("Já existe Nota de compra com essa mesma descrição : " + notaFiscalCompra.getDescricaoObs());
			   }
			}	
		}
		
		if (notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0) {
			throw new ExceptionLoja("A Pessoa Juridica da nota fiscal deve ser informada.");
		}
		
		if (notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0) {
			throw new ExceptionLoja("A empresa responsável deve ser infromada.");
		}
		
		if (notaFiscalCompra.getContaPagar() == null || notaFiscalCompra.getContaPagar().getId() <= 0) {
			throw new ExceptionLoja("A cponta a pagar da nota deve ser informada.");
		}
		
		NotaFiscalCompra notaFiscalCompraSalva = notaFiscalCompraRepository.save(notaFiscalCompra);
		
		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompraSalva, HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteNotaFiscalCompraPorId/{id}")
	public ResponseEntity<?> deleteNotaFiscalCompraPorId(@PathVariable("id") Long id) { 
		
		
		notaFiscalCompraRepository.deleteItemNotaFiscalCompra(id);/*Delete os filhos*/
		notaFiscalCompraRepository.deleteById(id); /*Deleta o pai*/
		
		return new ResponseEntity("Nota Fiscal Compra Removida",HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/obterNotaFiscalCompra/{id}")
	public ResponseEntity<NotaFiscalCompra> obterNotaFiscalCompra(@PathVariable("id") Long id) throws ExceptionLoja { 
		
		NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);
		
		if (notaFiscalCompra == null) {
			throw new ExceptionLoja("Não encontrou Nota Fiscal com código: " + id);
		}
		
		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompra, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/obterNotaFiscalCompraDaVenda/{idvenda}")
	public ResponseEntity<List<NotaFiscalVenda>> obterNotaFiscalCompraDaVenda(@PathVariable("idvenda") Long idvenda) throws ExceptionLoja { 
		
		List<NotaFiscalVenda> notaFiscalCompra = notaFiscalVendaRepository.buscaNotaPorVenda(idvenda);
		
		if (notaFiscalCompra == null) {
			throw new ExceptionLoja("Não encontrou Nota Fiscal de venda com código da venda: " + idvenda);
		}
		
		return new ResponseEntity<List<NotaFiscalVenda>>(notaFiscalCompra, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@GetMapping(value = "**/obterNotaFiscalCompraDaVendaUnico/{idvenda}")
	public ResponseEntity<NotaFiscalVenda> obterNotaFiscalCompraDaVendaUnico(@PathVariable("idvenda") Long idvenda) throws ExceptionLoja { 
		
		NotaFiscalVenda notaFiscalCompra = notaFiscalVendaRepository.buscaNotaPorVendaUnica(idvenda);
		
		if (notaFiscalCompra == null) {
			throw new ExceptionLoja("Não encontrou Nota Fiscal de venda com código da venda: " + idvenda);
		}
		
		return new ResponseEntity<NotaFiscalVenda>(notaFiscalCompra, HttpStatus.OK);
	}

	
	@ResponseBody
	@GetMapping(value = "**/buscarNotaFiscalPorDesc/{desc}")
	public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalPorDesc(@PathVariable("desc") String desc) { 
		
		List<NotaFiscalCompra>  notaFiscalCompras = notaFiscalCompraRepository.buscaNotaDesc(desc.toUpperCase().trim());
		
		return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompras,HttpStatus.OK);
	}


}
