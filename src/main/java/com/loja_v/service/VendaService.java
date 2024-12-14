package com.loja_v.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class VendaService {
	

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

}
