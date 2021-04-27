package br.com.suleimanmoraes.igrejanewservice.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.suleimanmoraes.igrejanewservice.api.model.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

	Endereco findTopByPessoasId(Long pessoaId);
}
