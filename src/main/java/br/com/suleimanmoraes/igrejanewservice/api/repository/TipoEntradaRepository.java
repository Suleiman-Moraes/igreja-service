package br.com.suleimanmoraes.igrejanewservice.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.model.TipoEntrada;

public interface TipoEntradaRepository extends JpaRepository<TipoEntrada, Long>{

	List<TipoEntrada> findByAtivoOrderByNomeAsc(AtivoInativoEnum ativo);
}
