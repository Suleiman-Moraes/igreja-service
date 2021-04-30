package br.com.suleimanmoraes.igrejanewservice.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	boolean existsByIdAndUsuarioId(Long id, Long usuarioId);

	boolean existsByIdAndIgrejaId(Long id, Long igrejaId);

	Pessoa findTopByUsuarioId(Long usuarioId);

	boolean existsByCpfAndIdNot(String cpf, Long id);

	@Query(value = "SELECT new Pessoa(p.id, p.nome) FROM Pessoa p WHERE p.igreja.id = ?1 AND p.ativo = ?2 ORDER BY p.nome ASC")
	List<Pessoa> findByIgrejaIdAndAtivo(Long igrejaId, AtivoInativoEnum ativo);
}
