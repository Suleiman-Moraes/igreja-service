package br.com.suleimanmoraes.igrejanewservice.api.service;

import java.util.List;

import br.com.suleimanmoraes.igrejanewservice.api.dto.EnderecoDto;
import br.com.suleimanmoraes.igrejanewservice.api.interfaces.CrudPadraoService;
import br.com.suleimanmoraes.igrejanewservice.api.model.Endereco;

public interface EnderecoService extends CrudPadraoService<Endereco>{

	List<String> validar(EnderecoDto objeto, List<String> erros) throws Exception;

	EnderecoDto tratar(Endereco endereco);

	Endereco findByPessoasId(Long pessoaId);

	Endereco tratarEnderecoDtoPreSavePessoa(EnderecoDto objeto, Long pessoaId, Long igrejaId);
}
