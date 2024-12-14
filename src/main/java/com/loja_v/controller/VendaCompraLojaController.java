package com.loja_v.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.loja_v.ExceptionLoja;
import com.loja_v.model.Endereco;
import com.loja_v.model.ItemVendaLoja;
import com.loja_v.model.PessoaFisica;
import com.loja_v.model.VendaCompraLoja;
import com.loja_v.model.dto.ItemVendaDTO;
import com.loja_v.model.dto.VendaCompraLojaDTO;
import com.loja_v.repository.EnderecoRepository;
import com.loja_v.repository.NotaFiscalVendaRepository;
import com.loja_v.repository.VendaCompraLojaRepository;

@RestController
public class VendaCompraLojaController {
	
	@Autowired
	private VendaCompraLojaRepository vendaCompraLojaRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private NotaFiscalVendaRepository notaFiscalVendaRepository;
	
	@ResponseBody
	@PostMapping(value = "**/salvarVendaLoja")
	public ResponseEntity<VendaCompraLojaDTO> salvarVendaLoja(@RequestBody @Valid VendaCompraLoja vendaCompraLoja) throws ExceptionLoja {
		
		vendaCompraLoja.getPessoa().setEmpresa(vendaCompraLoja.getEmpresa());
		PessoaFisica pessoaFisica = pessoaController.salvarPf(vendaCompraLoja.getPessoa()).getBody();
		vendaCompraLoja.setPessoa(pessoaFisica);
		
		vendaCompraLoja.getEnderecoCobranca().setPessoa(pessoaFisica);
		vendaCompraLoja.getEnderecoCobranca().setEmpresa(vendaCompraLoja.getEmpresa());
		Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLoja.getEnderecoCobranca());
		vendaCompraLoja.setEnderecoCobranca(enderecoCobranca);
		
		vendaCompraLoja.getEnderecoEntrega().setPessoa(pessoaFisica);
		vendaCompraLoja.getEnderecoEntrega().setEmpresa(vendaCompraLoja.getEmpresa());
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLoja.getEnderecoEntrega());
		vendaCompraLoja.setEnderecoEntrega(enderecoEntrega);
		
		vendaCompraLoja.getNotaFiscalVenda().setEmpresa(vendaCompraLoja.getEmpresa());
		
		for (int i = 0; i < vendaCompraLoja.getItemVendaLojas().size(); i++) {
			vendaCompraLoja.getItemVendaLojas().get(i).setEmpresa(vendaCompraLoja.getEmpresa());
			vendaCompraLoja.getItemVendaLojas().get(i).setVendaCompraLoja(vendaCompraLoja);
		}
		
		/*Salva primeiro a venda e todo os dados*/
		vendaCompraLoja = vendaCompraLojaRepository.saveAndFlush(vendaCompraLoja);
		
		/*Associa a venda gravada no banco com a nota fiscal*/
		vendaCompraLoja.getNotaFiscalVenda().setVendaCompraLoja(vendaCompraLoja);
		
		/*Persiste novamente as nota fiscal novamente pra ficar amarrada na venda*/
		 notaFiscalVendaRepository.saveAndFlush(vendaCompraLoja.getNotaFiscalVenda());
		
		 VendaCompraLojaDTO compraLojaDTO = new VendaCompraLojaDTO();
		 compraLojaDTO.setValorTotal(vendaCompraLoja.getValorTotal());
		 compraLojaDTO.setPessoa(vendaCompraLoja.getPessoa());

		 compraLojaDTO.setEntrega(vendaCompraLoja.getEnderecoEntrega());
		 compraLojaDTO.setCobranca(vendaCompraLoja.getEnderecoCobranca());

		 compraLojaDTO.setValorDesc(vendaCompraLoja.getValorDesconto());
		 compraLojaDTO.setValorFrete(vendaCompraLoja.getValorFret());
		 compraLojaDTO.setId(vendaCompraLoja.getId());

			for (ItemVendaLoja item : vendaCompraLoja.getItemVendaLojas()) {

				ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
				itemVendaDTO.setQuantidade(item.getQuantidade());
				itemVendaDTO.setProduto(item.getProduto());

				compraLojaDTO.getItemVendaLoja().add(itemVendaDTO);
			}

			return new ResponseEntity<VendaCompraLojaDTO>(compraLojaDTO, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaVendaId/{id}")
	public ResponseEntity<VendaCompraLojaDTO> consultaVendaId(@PathVariable("id") Long idVenda) {


		VendaCompraLoja compraLoja = vendaCompraLojaRepository.findById(idVenda)
				.orElse(new VendaCompraLoja());


		VendaCompraLojaDTO compraLojaVirtualDTO = new VendaCompraLojaDTO();

		compraLojaVirtualDTO.setValorTotal(compraLoja.getValorTotal());
		compraLojaVirtualDTO.setPessoa(compraLoja.getPessoa());

		compraLojaVirtualDTO.setEntrega(compraLoja.getEnderecoEntrega());
		compraLojaVirtualDTO.setCobranca(compraLoja.getEnderecoCobranca());

		compraLojaVirtualDTO.setValorDesc(compraLoja.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(compraLoja.getValorFret());
		compraLojaVirtualDTO.setId(compraLoja.getId());

		for (ItemVendaLoja item : compraLoja.getItemVendaLojas()) {

			ItemVendaDTO itemVendaDTO = new ItemVendaDTO();
			itemVendaDTO.setQuantidade(item.getQuantidade());
			itemVendaDTO.setProduto(item.getProduto());

			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaDTO);
		}


		return new ResponseEntity<VendaCompraLojaDTO>(compraLojaVirtualDTO, HttpStatus.OK);
	}



}
