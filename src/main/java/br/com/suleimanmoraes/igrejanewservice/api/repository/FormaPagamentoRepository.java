package br.com.suleimanmoraes.igrejanewservice.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.model.FormaPagamento;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long>{

	@Query(value = "SELECT new FormaPagamento(fp.id, fp.nome) FROM FormaPagamento fp WHERE fp.ativo = ?1 ORDER BY fp.nome ASC")
	List<FormaPagamento> findByAtivoOrderByNomeAsc(AtivoInativoEnum ativo);
}
