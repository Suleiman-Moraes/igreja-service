package br.com.suleimanmoraes.igrejanewservice.api.service;

import org.springframework.data.domain.Page;

import br.com.suleimanmoraes.igrejanewservice.api.dto.GraficoDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.SaidaInformacaoDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterSaidaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.listagem.SaidaListagemDto;
import br.com.suleimanmoraes.igrejanewservice.api.interfaces.CrudPadraoService;
import br.com.suleimanmoraes.igrejanewservice.api.model.Saida;

public interface SaidaService extends CrudPadraoService<Saida> {

	Boolean ativar(Long id) throws Exception;

	Page<SaidaListagemDto> findByParams(FilterSaidaDto filter);

	SaidaInformacaoDto getInformacao(FilterSaidaDto filter);

	void vericarIgreja(Long id);

	GraficoDto montarGraficoAnual(Integer ano);
}
