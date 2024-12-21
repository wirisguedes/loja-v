package com.loja_v.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.loja_v.model.dto.ObejtoRequisicaoRelatorioProdCompraNotaFiscalDTO;
import com.loja_v.model.dto.ObejtoRequisicaoRelatorioProdutoAlertaEstoqueDTO;

@Service
public class NotaFiscalCompraService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<ObejtoRequisicaoRelatorioProdCompraNotaFiscalDTO> gerarRelatorioProdCompraNota(
			ObejtoRequisicaoRelatorioProdCompraNotaFiscalDTO obejtoRequisicaoRelatorioProdCompraNotaFiscalDto) {

		List<ObejtoRequisicaoRelatorioProdCompraNotaFiscalDTO> retorno = new ArrayList<ObejtoRequisicaoRelatorioProdCompraNotaFiscalDTO>();

		String sql = "select p.id as codigoProduto, p.nome as nomeProduto, "
				+ " p.valor_venda as valorVendaProduto, ntp.quantidade as quantidadeComprada, "
				+ " pj.id as codigoFornecedor, pj.nome as nomeFornecedor,cfc.data_compra as dataCompra "
				+ " from nota_fiscal_compra as cfc "
				+ " inner join nota_item_produto as ntp on  cfc.id = nota_fiscal_compra_id "
				+ " inner join produto as p on p.id = ntp.produto_id "
				+ " inner join pessoa_juridica as pj on pj.id = cfc.pessoa_id where ";

		sql += " cfc.data_compra >='" + obejtoRequisicaoRelatorioProdCompraNotaFiscalDto.getDataInicial() + "' and ";
		sql += " cfc.data_compra <= '" + obejtoRequisicaoRelatorioProdCompraNotaFiscalDto.getDataFinal() + "' ";

		if (!obejtoRequisicaoRelatorioProdCompraNotaFiscalDto.getCodigoNota().isEmpty()) {
			sql += " and cfc.id = " + obejtoRequisicaoRelatorioProdCompraNotaFiscalDto.getCodigoNota() + " ";
		}

		if (!obejtoRequisicaoRelatorioProdCompraNotaFiscalDto.getCodigoProduto().isEmpty()) {
			sql += " and p.id = " + obejtoRequisicaoRelatorioProdCompraNotaFiscalDto.getCodigoProduto() + " ";
		}

		if (!obejtoRequisicaoRelatorioProdCompraNotaFiscalDto.getNomeProduto().isEmpty()) {
			sql += " upper(p.nome) like upper('%" + obejtoRequisicaoRelatorioProdCompraNotaFiscalDto.getNomeProduto()
					+ "')";
		}

		if (!obejtoRequisicaoRelatorioProdCompraNotaFiscalDto.getNomeFornecedor().isEmpty()) {
			sql += " upper(pj.nome) like upper('%"
					+ obejtoRequisicaoRelatorioProdCompraNotaFiscalDto.getNomeFornecedor() + "')";
		}

		retorno = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper(ObejtoRequisicaoRelatorioProdCompraNotaFiscalDTO.class));

		return retorno;
	}

	public List<ObejtoRequisicaoRelatorioProdutoAlertaEstoqueDTO> gerarRelatorioAlertaEstoque(
			ObejtoRequisicaoRelatorioProdutoAlertaEstoqueDTO alertaEstoque) {

		List<ObejtoRequisicaoRelatorioProdutoAlertaEstoqueDTO> retorno = new ArrayList<ObejtoRequisicaoRelatorioProdutoAlertaEstoqueDTO>();

		String sql = "select p.id as codigoProduto, p.nome as nomeProduto, "
				+ " p.valor_venda as valorVendaProduto, ntp.quantidade as quantidadeComprada, "
				+ " pj.id as codigoFornecedor, pj.nome as nomeFornecedor,cfc.data_compra as dataCompra, "
				+ " p.qtd_estoque as qtdEstoque, p.qtd_alerta_estoque as qtdAlertaEstoque "
				+ " from nota_fiscal_compra as cfc "
				+ " inner join nota_item_produto as ntp on  cfc.id = nota_fiscal_compra_id "
				+ " inner join produto as p on p.id = ntp.produto_id "
				+ " inner join pessoa_juridica as pj on pj.id = cfc.pessoa_id where ";

		sql += " cfc.data_compra >='" + alertaEstoque.getDataInicial() + "' and ";
		sql += " cfc.data_compra <= '" + alertaEstoque.getDataFinal() + "' ";
		sql += " and p.alerta_qtd_estoque = true and p.qtd_estoque <= p.qtd_alerta_estoque ";

		if (!alertaEstoque.getCodigoNota().isEmpty()) {
			sql += " and cfc.id = " + alertaEstoque.getCodigoNota() + " ";
		}

		if (!alertaEstoque.getCodigoProduto().isEmpty()) {
			sql += " and p.id = " + alertaEstoque.getCodigoProduto() + " ";
		}

		if (!alertaEstoque.getNomeProduto().isEmpty()) {
			sql += " upper(p.nome) like upper('%" + alertaEstoque.getNomeProduto() + "')";
		}

		if (!alertaEstoque.getNomeFornecedor().isEmpty()) {
			sql += " upper(pj.nome) like upper('%" + alertaEstoque.getNomeFornecedor() + "')";
		}

		retorno = jdbcTemplate.query(sql,
				new BeanPropertyRowMapper(ObejtoRequisicaoRelatorioProdutoAlertaEstoqueDTO.class));

		return retorno;
	}

}
