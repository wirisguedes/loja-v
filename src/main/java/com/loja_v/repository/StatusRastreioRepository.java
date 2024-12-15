package com.loja_v.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.loja_v.model.StatusRastreio;

@Repository
public interface StatusRastreioRepository extends JpaRepository<StatusRastreio, Long>{
	
	@Query(value = "select s from StatusRastreio s where s.vendaCompraLoja.id = ?1 order by s.id")
	public List<StatusRastreio> listaRastreioVenda(Long idVenda);


}
