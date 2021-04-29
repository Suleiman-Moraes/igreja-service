package br.com.suleimanmoraes.igrejanewservice.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.suleimanmoraes.igrejanewservice.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

	boolean existsByIdAndUsuarioId(Long id, Long usuarioId);

	boolean existsByIdAndIgrejaId(Long id, Long igrejaId);

	Pessoa findTopByUsuarioId(Long usuarioId);

	boolean existsByCpfAndIdNot(String cpf, Long id);
}
