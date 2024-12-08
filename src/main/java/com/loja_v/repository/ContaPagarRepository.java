package com.loja_v.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.loja_v.model.ContaPagar;

@Repository
@Transactional
public interface ContaPagarRepository extends JpaRepository<ContaPagar, Long> {
	
	@Query("select a from ContaPagar a where upper(trim(a.descricao)) like %?1%")
	List<ContaPagar> buscarContaDesc(String desc);
	
	@Query("select a from ContaPagar a where a.pessoaFisica.id = ?1")
	List<ContaPagar> buscarContaPorPessoa(Long idPessoa);
	
	@Query("select a from ContaPagar a where a.pessoa_fornecedor.id = ?1")
	List<ContaPagar> buscarContaPorFornecedor(Long idPessoaForncedor);

	@Query("select a from ContaPagar a where a.empresa.id = ?1")
	List<ContaPagar> buscarContaPorEmpresa(Long idEmpresa);

}
