package com.loja_v.service;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.loja_v.model.VendaCompraLoja;


@Service
public class VendaService {
	

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void exclusaoLogica(Long idVenda) {
		String sql = "begin; update venda_compra_loja set excluido = true where id = " + idVenda + "; commit;";
		jdbcTemplate.execute(sql);
	}


	public void exclusaoTotalVendaBanco(Long idVenda) {
		
		String value = 
		                  " begin;"
		      			+ " UPDATE nota_fiscal_venda set venda_compra_loja_id = null where venda_compra_loja_id = "+idVenda+"; "
		      			+ " delete from nota_fiscal_venda where venda_compra_loja_id = "+idVenda+"; "
		      			+ " delete from item_venda_loja where venda_compra_loja_id = "+idVenda+"; "
		      			+ " delete from status_rastreio where venda_compra_loja_id = "+idVenda+"; "
		      			+ " delete from venda_compra_loja where id = "+idVenda+"; "
		      			+ " commit; ";
		
		jdbcTemplate.execute(value);
	}

	
	public void ativarRegistroVenda(Long idVenda) {
		String sql = "begin; update venda_compra_loja set excluido = false where id = " + idVenda + "; commit;";
		jdbcTemplate.execute(sql);
	}

	/*HQL (Hibernate) ou JPQL (JPA ou Spring Data)*/
	@SuppressWarnings("unchecked")
	public List<VendaCompraLoja> consultaVendaFaixaData(String data1, String data2){
		
		String sql = "select distinct(i.vendaCompraLoja) from ItemVendaLoja i "
				+ " where i.vendaCompraLoja.excluido = false "
				+ " and i.vendaCompraLoja.dataVenda >= '" + data1 + "'"
				+ " and i.vendaCompraLoja.dataVenda <= '" + data2 + "'";
		
		return entityManager.createQuery(sql).getResultList();
		
	}

}
