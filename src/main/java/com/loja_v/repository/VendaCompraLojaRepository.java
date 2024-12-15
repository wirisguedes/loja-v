package com.loja_v.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.loja_v.model.VendaCompraLoja;

@Repository
@Transactional
public interface VendaCompraLojaRepository extends JpaRepository<VendaCompraLoja, Long> {

	@Query(value = "select a from VendaCompraLoja a where a.id = ?1 and a.excluido = false")
	VendaCompraLoja findByIdExclusao(Long id);

	@Query(value = "select i.vendaCompraLoja from ItemVendaLoja i where i.vendaCompraLoja.excluido = false and i.produto.id = ?1")
	List<VendaCompraLoja> vendaPorProduto(Long idProduto);

	@Query(value = "select distinct(i.vendaCompraLoja) from ItemVendaLoja i "
			+ " where i.vendaCompraLoja.excluido = false and upper(trim(i.produto.nome)) like %?1%")
	List<VendaCompraLoja> vendaPorNomeProduto(String valor);
	
	@Query(value = "select distinct(i.vendaCompraLoja) from ItemVendaLoja i "
			+ " where i.vendaCompraLoja.excluido = false and vendaCompraLoja.pessoa.id = ?1")
	List<VendaCompraLoja> vendaPorCliente(Long idCliente);

	@Query(value = "select distinct(i.vendaCompraLoja) from ItemVendaLoja i "
			+ " where i.vendaCompraLoja.excluido = false and upper(trim(i.vendaCompraLoja.pessoa.nome)) like %?1%")
	List<VendaCompraLoja> vendaPorNomeCliente(String nomepessoa);

	@Query(value = "select distinct(i.vendaCompraLoja) from ItemVendaLoja i "
			+ " where i.vendaCompraLoja.excluido = false and upper(trim(i.vendaCompraLoja.enderecoCobranca.ruaLogra)) "
			+ " like %?1%")
	List<VendaCompraLoja> vendaPorEndereCobranca(String enderecocobranca);

	@Query(value = "select distinct(i.vendaCompraLoja) from ItemVendaLoja i "
			+ " where i.vendaCompraLoja.excluido = false and upper(trim(i.vendaCompraLoja.enderecoEntrega.ruaLogra)) "
			+ " like %?1%")
	List<VendaCompraLoja> vendaPorEnderecoEntrega(String enderecoentrega);
	
	@Query(value="select distinct(i.vendaCompraLoja) from ItemVendaLoja i "
			+ " where i.vendaCompraLoja.excluido = false "
			+ " and i.vendaCompraLoja.dataVenda >= ?1 "
			+ " and i.vendaCompraLoja.dataVenda <= ?2 ")
	List<VendaCompraLoja> consultaVendaFaixaData(Date data1, Date data2);


}
