package br.com.suleimanmoraes.igrejanewservice.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.suleimanmoraes.igrejanewservice.api.model.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

	@Query(value = "SELECT COUNT(*) > 0 FROM permissao p INNER JOIN usuario_permissao up ON p.id = up.id_permissao WHERE up.id_usuario = ?1", nativeQuery = true)
	boolean existsByUsuariosId(Long usuarioId);

	boolean existsByIdAndUsuariosId(Long id, Long usuarioId);

	List<Permissao> findByCargosPessoasId(Long pessoaId);

	List<Permissao> findByCargosId(Long cargoId);
}
