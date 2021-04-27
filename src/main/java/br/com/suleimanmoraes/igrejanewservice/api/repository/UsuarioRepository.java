package br.com.suleimanmoraes.igrejanewservice.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.suleimanmoraes.igrejanewservice.api.dto.UsuarioLogadoDto;
import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query(value = "SELECT u.id FROM Usuario u WHERE u.login = ?1")
	long findIdByLogin(String login);

	@Query(value = "SELECT u FROM Usuario u LEFT JOIN u.igreja i WHERE u.login = ?1 AND u.ativo = ?2 AND (i IS NULL OR i.ativo = ?3)")
	Optional<Usuario> findByLoginAndAtivoAndIgrejaAtivo(String login, AtivoInativoEnum ativo,
			AtivoInativoEnum igrejaAtivo);

	List<Usuario> findByPessoaCargoIdAndIdNot(Long cargoId, Long id);

	boolean existsByPessoaId(Long pessoaId);

	@Query(value = "SELECT u.senha FROM Usuario u WHERE u.id = ?1")
	String findSenhaById(Long id);

	boolean existsByLoginAndIdNot(String login, Long id);

	Usuario findTopByPessoaId(Long pessoaId);

	@Query(value = "SELECT u.id FROM Usuario u WHERE u.pessoa.id = ?1 AND u.id != ?2")
	long findIdTopByPessoaIdAndIdNot(Long pessoaId, Long id);

	@Query(value = "SELECT new br.com.suleimanmoraes.igrejanewservice.api.dto.UsuarioLogadoDto"
			+ "(u.id, u.login, p.nome, i.nome, i.id) "
			+ "FROM Usuario u LEFT JOIN u.pessoa p LEFT JOIN u.igreja i WHERE u.id = ?1")
	UsuarioLogadoDto findUsuarioLogadoDtoById(Long id);
}
