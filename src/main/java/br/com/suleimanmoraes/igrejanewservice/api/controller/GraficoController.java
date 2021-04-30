package br.com.suleimanmoraes.igrejanewservice.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.suleimanmoraes.igrejanewservice.api.dto.GraficoDto;
import br.com.suleimanmoraes.igrejanewservice.api.service.GraficoService;

@RestController
@RequestMapping("/api/grafico")
public class GraficoController {

	@Autowired
	private GraficoService service;

	@GetMapping(value = "/anual")
	public ResponseEntity<List<GraficoDto>> montarGraficoAnual(HttpServletRequest request,
			@RequestParam(name = "ano", required = false, defaultValue = "0") int ano) throws Exception {
		List<GraficoDto> retorno = service.montarGraficoAnual(ano);
		return ResponseEntity.ok(retorno);
	}
}
