package com.loja_v.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.loja_v.model.NotaFiscalVenda;

@Repository
public interface NotaFiscalVendaRepository extends JpaRepository<NotaFiscalVenda, Long> {
	
	@Query(value = "select n from NotaFiscalVenda n where n.vendaCompraLoja.id = ?1")
	List<NotaFiscalVenda> buscaNotaPorVenda(Long idVenda);
	
	@Query(value = "select n from NotaFiscalVenda n where n.vendaCompraLoja.id = ?1")
	NotaFiscalVenda buscaNotaPorVendaUnica(Long idVenda);


}
