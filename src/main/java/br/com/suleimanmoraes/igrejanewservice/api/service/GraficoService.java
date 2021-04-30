package br.com.suleimanmoraes.igrejanewservice.api.service;

import java.util.List;

import br.com.suleimanmoraes.igrejanewservice.api.dto.GraficoDto;

public interface GraficoService {

	List<GraficoDto> montarGraficoAnual(Integer ano);
}
