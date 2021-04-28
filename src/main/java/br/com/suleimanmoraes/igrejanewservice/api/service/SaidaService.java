package br.com.suleimanmoraes.igrejanewservice.api.service;

import org.springframework.data.domain.Page;

import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterSaidaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.listagem.SaidaListagemDto;
import br.com.suleimanmoraes.igrejanewservice.api.interfaces.CrudPadraoService;
import br.com.suleimanmoraes.igrejanewservice.api.model.Saida;

public interface SaidaService extends CrudPadraoService<Saida> {

	Boolean ativar(Long id) throws Exception;

	Page<SaidaListagemDto> findByParams(FilterSaidaDto filter);
}
