package com.loja_v.repository;

import java.util.List;

import javax.transaction.Transactional;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



import com.loja_v.model.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
	
	@Query(value = "select u from Usuario u where u.empresa.id = ?1")
	List<Usuario> listUserByEmpresa(Long idEmpresa);
	
	@Query(value = "select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);
	
	@Query(value = "select u from Usuario u where u.dataAtualSenha <= current_date - 90")
	List<Usuario> usuarioSenhaVencida();

	@Query(value = "select u from Usuario u where u.pessoa.id = ?1 or u.login =?2")
	Usuario findUserByPessoa(Long id, String email);

	@Query(value = "select constraint_name from information_schema.constraint_column_usage where table_name = 'usuarios_acesso' and column_name = 'acesso_id' and constraint_name <> 'unique_acesso_user';", nativeQuery = true)
	String consultaConstraintAcesso();

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into usuarios_acesso(usuario_id, acesso_id) values (?1, (select id from acesso where descricao = 'ROLE_USER5'))")
	void insereAcessoUser(Long iduser);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into usuarios_acesso(usuario_id, acesso_id) values (?1, (select id from acesso where descricao = ?2 limit 1))")
	void insereAcessoUserPj(Long iduser, String acesso);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Usuario u SET u.senha = ?1 WHERE u.login = ?2")
	void updateSenhaUser(String senha, String login);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Usuario u SET u.senha = ?1 WHERE u.id = ?2")
	void updateSenhaUserId(String senha, Long id);
	
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Usuario u SET u.login = ?1 WHERE u.id = ?2")
	void updateLoginUser(String login, Long id);
	
	@Query(nativeQuery = true, value = "select count(1) > 0 from usuario where senha = ?1 and id = ?2")
	Boolean senhaIgual(String senha, Long id);
	
	@Query(nativeQuery = true, value = "select count(1) > 0 from usuarios_acesso where acesso_id = ?1 and usuario_id = ?2")
	Boolean possuiAcesso(Long idAcesso, Long idUser);
	
	@Transactional
	@Modifying(flushAutomatically = true)
	@Query(value = "delete from usuarios_acesso where usuario_id in (select distinct usuario_id from usuarios_acesso  where usuario_id in (select id from usuario where pessoa_id = ?1))", nativeQuery = true)
	void deleteAcessoUserByPessoa(Long idPessoa);
	
	@Transactional
	@Modifying(flushAutomatically = true)
	@Query(value = "delete from usuario where pessoa_id = ?1", nativeQuery = true)
	void deleteByPessoa(Long idEmpresa);

	@Transactional
	@Modifying(flushAutomatically = true)
	@Query(value = "delete from usuarios_acesso where acesso_id = ?1 and usuario_id = ?2", nativeQuery = true)
	void deleteByAcesso(Long idAcesso, Long idUser);

	@Transactional
	@Modifying(flushAutomatically = true)
	@Query(value = "INSERT INTO usuarios_acesso(acesso_id, usuario_id) VALUES (?1, ?2)", nativeQuery = true)
	void addAcesso(Long idAcesso, Long idUser);

}
