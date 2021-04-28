package br.com.suleimanmoraes.igrejanewservice.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.suleimanmoraes.igrejanewservice.api.model.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

	List<Estado> findByOrderByNomeAsc();
}
