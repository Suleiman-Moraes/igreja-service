package br.com.suleimanmoraes.igrejanewservice.api.service;

import org.springframework.data.domain.Page;

import br.com.suleimanmoraes.igrejanewservice.api.dto.EntradaInformacaoDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.GraficoDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterEntradaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.listagem.EntradaListagemDto;
import br.com.suleimanmoraes.igrejanewservice.api.interfaces.CrudPadraoService;
import br.com.suleimanmoraes.igrejanewservice.api.model.Entrada;

public interface EntradaService extends CrudPadraoService<Entrada> {

	Boolean ativar(Long id) throws Exception;

	Page<EntradaListagemDto> findByParams(FilterEntradaDto filter);
	
	EntradaInformacaoDto getInformacao(FilterEntradaDto filter);

	GraficoDto montarGraficoAnual(Integer ano);

	void vericarIgreja(Long id);
}
