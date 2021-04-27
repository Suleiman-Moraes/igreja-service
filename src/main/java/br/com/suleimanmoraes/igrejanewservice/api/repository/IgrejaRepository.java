package br.com.suleimanmoraes.igrejanewservice.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.suleimanmoraes.igrejanewservice.api.model.Igreja;

public interface IgrejaRepository extends JpaRepository<Igreja, Long>{

	Igreja findTopByUsuariosId(Long usuarioId);
}
