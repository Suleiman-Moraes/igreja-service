package br.com.suleimanmoraes.igrejanewservice.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.suleimanmoraes.igrejanewservice.api.model.Estado;
import br.com.suleimanmoraes.igrejanewservice.api.service.EstadoService;
import br.com.suleimanmoraes.igrejanewservice.api.util.RestControllerUtil;
import lombok.Getter;

@RestController
@RequestMapping("/api/estado")
public class EstadoController {

	@Getter
	@Autowired
	private EstadoService service;

	@GetMapping
	public ResponseEntity<List<Estado>> findAll(HttpServletRequest request) {
		return RestControllerUtil.findAllCompleto(service);
	}
}
