package com.loja_v.repository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.loja_v.model.Acesso;
import com.loja_v.model.PessoaFisica;
import com.loja_v.model.PessoaJuridica;

@Repository
public interface PessoaRepository extends CrudRepository<PessoaJuridica, Long> {
	
	@Query("select a from PessoaJuridica a where upper(trim(a.nomeFantasia)) like %?1% and a.empresa.id = ?2")
	public List<PessoaJuridica> buscarPessoaJuridicaDesc(String nomeFantasia, Long empresa);
	
	@Query("select a from PessoaFisica a where upper(trim(a.nome)) like %?1% and a.empresa.id = ?2")
	public List<PessoaFisica> buscarPessoaFisicaDesc(String nomeFantasia, Long empresa);
	
	@Query("select p from PessoaFisica p where id = ?1")
	public PessoaFisica findByIdPf(Long idPessoaFisica);
	
	@Query(nativeQuery = true, value = "select cast((count(1) / 5) as integer) + 1 as qtdpagina  from  pessoa_juridica where empresa_id = ?1")
	public Integer qtdPagina(Long idEmpresa);
	
	@Query(nativeQuery = true, value = "select cast((count(1) / 5) as integer) + 1 as qtdpagina  from  pessoa_fisica where empresa_id = ?1")
	public Integer qtdPaginaPf(Long idEmpresa);
	
	@Query(value = "select a from PessoaJuridica a where a.empresa.id = ?1 ")
	public List<PessoaJuridica> findPorPage(Long idEmpresa, Pageable pageable);
	
	@Query(value = "select a from PessoaFisica a where a.empresa.id = ?1 ")
	public List<PessoaFisica> findPorPagePf(Long idEmpresa, Pageable pageable);
	
	@Query(value = "select pj from PessoaJuridica pj where upper(trim(pj.nome)) like %?1%")
	public List<PessoaJuridica> pesquisaPorNomePJ(String nome);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public PessoaJuridica existeCnpjCadastrado(String cnpj);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.cnpj = ?1")
	public List<PessoaJuridica> existeCnpjCadastradoList(String cnpj);
	
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public PessoaFisica existeCpfCadastrado(String cpf);
	
	@Query(value = "select pf from PessoaFisica pf where pf.cpf = ?1")
	public List<PessoaFisica> existeCpfCadastradoList(String cpf);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
	public PessoaJuridica existeInscEstadualCadastrado(String inscEstadual);
	
	@Query(value = "select pj from PessoaJuridica pj where pj.inscEstadual = ?1")
	public List<PessoaJuridica> existeInscEstadualCadastradoList(String inscEstadual);
	
	@Transactional
	@Modifying(flushAutomatically = true)
	@Query(value = "delete from pessoa_fisica where id = ?1", nativeQuery = true)
	public void deleteByIdPf(Long idPessoaFisica);

}
