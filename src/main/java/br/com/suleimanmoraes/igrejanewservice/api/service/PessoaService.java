package br.com.suleimanmoraes.igrejanewservice.api.service;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.suleimanmoraes.igrejanewservice.api.dto.PessoaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterPessoaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.listagem.PessoaListagemDto;
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

	Page<PessoaListagemDto> findByParams(FilterPessoaDto filter);

	Boolean ativar(Long id) throws Exception;

	List<Pessoa> findByIgrejaIdAndAtivo(Long igrejaId);
}
