package com.loja_v.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.loja_v.model.CupDesc;

@Repository
public interface CupDescRepository extends JpaRepository<CupDesc, Long> {
	
	@Query(value = "select c from CupDesc c where c.empresa.id = ?1")
	public List<CupDesc> cupDescontoPorEmpresa(Long idEmpresa);

}
