package com.loja_v.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.loja_v.model.NotaFiscalCompra;
import com.loja_v.model.NotaItemProduto;

@RestController
@Transactional
public interface NotaItemProdutoRepository extends JpaRepository<NotaItemProduto, Long>{
	
	@Query("select a from NotaItemProduto a where a.produto.id = ?1 and a.notaFiscalCompra.id = ?2")
	List<NotaItemProduto> buscaNotaItemPorProdutoNota(Long idProduto, Long idNotaFiscal);
	
	@Query("select a from NotaItemProduto a where a.produto.id = ?1")
	List<NotaItemProduto> buscaNotaItemPorProduto(Long idProduto);
	
	@Query("select a from NotaItemProduto a where a.notaFiscalCompra.id = ?2")
	List<NotaItemProduto> buscaNotaItemPorNotaFiscal(Long idNotaFiscal);
	
	@Query("select a from NotaItemProduto a where a.empresa.id = ?2")
	List<NotaFiscalCompra> buscaNotaItemPorEmpresa(Long idEmpresa);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "delete from nota_item_produto where id = ?1")
	void deleteByIdNotaItem(Long id);

}
