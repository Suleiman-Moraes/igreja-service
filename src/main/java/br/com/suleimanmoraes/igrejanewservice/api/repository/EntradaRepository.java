package br.com.suleimanmoraes.igrejanewservice.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.suleimanmoraes.igrejanewservice.api.model.Entrada;

public interface EntradaRepository extends JpaRepository<Entrada, Long>{

	boolean existsByIdAndIgrejaId(Long id, Long igrejaId);
}
