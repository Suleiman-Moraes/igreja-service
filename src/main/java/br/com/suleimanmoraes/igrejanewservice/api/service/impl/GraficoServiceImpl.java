package br.com.suleimanmoraes.igrejanewservice.api.service.impl;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.suleimanmoraes.igrejanewservice.api.dto.GraficoDto;
import br.com.suleimanmoraes.igrejanewservice.api.service.EntradaService;
import br.com.suleimanmoraes.igrejanewservice.api.service.GraficoService;
import br.com.suleimanmoraes.igrejanewservice.api.service.SaidaService;

@Service
public class GraficoServiceImpl implements GraficoService {

	private static final Log logger = LogFactory.getLog(GraficoService.class);
	
	@Autowired
	private EntradaService entradaService;

	@Autowired
	private SaidaService saidaService;

	@Override
	public List<GraficoDto> montarGraficoAnual(Integer ano) {
		try {
			List<GraficoDto> dtos = new LinkedList<>();
			ano = ano == null || ano <= 0 ? Calendar.getInstance().get(Calendar.YEAR) : ano;
			dtos.add(entradaService.montarGraficoAnual(ano));
			dtos.add(saidaService.montarGraficoAnual(ano));
			return dtos;
		} catch (Exception e) {
			logger.warn("montarGraficoAnual " + ExceptionUtils.getRootCauseMessage(e));
		}
		return null;
	}
}
