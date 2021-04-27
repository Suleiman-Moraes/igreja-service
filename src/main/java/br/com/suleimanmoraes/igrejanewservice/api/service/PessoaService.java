package br.com.suleimanmoraes.igrejanewservice.api.service;

import br.com.suleimanmoraes.igrejanewservice.api.dto.PessoaDto;
import br.com.suleimanmoraes.igrejanewservice.api.interfaces.CrudPadraoService;
import br.com.suleimanmoraes.igrejanewservice.api.model.Pessoa;

public interface PessoaService extends CrudPadraoService<Pessoa>{

	Pessoa saveNew(String nome);

	void vericarMe(Long id);

	void vericarIgreja(Long id);

	void validar(PessoaDto objeto) throws Exception;

	PessoaDto salvar(PessoaDto objeto);

	PessoaDto findDtoById(Long id);

	PessoaDto findDtoById();
}
