package br.com.suleimanmoraes.igrejanewservice.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.model.Igreja;

public interface IgrejaRepository extends JpaRepository<Igreja, Long>{

	Igreja findTopByUsuariosId(Long usuarioId);

	List<Igreja> findByAtivoOrderByNomeAsc(AtivoInativoEnum ativo);
}
