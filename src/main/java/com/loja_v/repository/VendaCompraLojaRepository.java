package com.loja_v.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.loja_v.model.VendaCompraLoja;

@Repository
@Transactional
public interface VendaCompraLojaRepository extends JpaRepository<VendaCompraLoja, Long>{
	
	
	@Query(value = "select a from VendaCompraLoja a where a.id = ?1 and a.excluido = false")
	VendaCompraLoja findByIdExclusao(Long id);
	
	@Query(value = "select i.vendaCompraLoja from ItemVendaLoja i where i.vendaCompraLoja.excluido = false and i.produto.id = ?1")
	List<VendaCompraLoja> vendaPorProduto(Long idProduto);

}
