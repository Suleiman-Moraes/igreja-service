package br.com.suleimanmoraes.igrejanewservice.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.model.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

	@Query(value = "SELECT new Cargo(c.id, c.nome, c.descricao, c.ativo) FROM Cargo c", countQuery = "SELECT COUNT(c) FROM Cargo c")
	Page<Cargo> findAll(Pageable pageable);

	@Query(value = "SELECT new Cargo(c.id, c.nome) FROM Cargo c WHERE c.ativo = ?1")
	List<Cargo> findByAtivo(AtivoInativoEnum ativo);
}
