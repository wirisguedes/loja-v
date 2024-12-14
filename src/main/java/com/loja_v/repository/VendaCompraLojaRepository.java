package com.loja_v.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.loja_v.model.VendaCompraLoja;

@Repository
@Transactional
public interface VendaCompraLojaRepository extends JpaRepository<VendaCompraLoja, Long>{

}
