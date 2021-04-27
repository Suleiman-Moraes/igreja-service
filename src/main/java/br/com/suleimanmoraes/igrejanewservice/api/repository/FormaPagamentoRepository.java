package br.com.suleimanmoraes.igrejanewservice.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.suleimanmoraes.igrejanewservice.api.model.FormaPagamento;

public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long>{

}
