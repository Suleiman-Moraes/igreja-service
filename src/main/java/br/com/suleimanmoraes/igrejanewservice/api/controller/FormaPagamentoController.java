package br.com.suleimanmoraes.igrejanewservice.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.suleimanmoraes.igrejanewservice.api.model.FormaPagamento;
import br.com.suleimanmoraes.igrejanewservice.api.service.FormaPagamentoService;
import br.com.suleimanmoraes.igrejanewservice.api.util.RestControllerUtil;
import lombok.Getter;

@RestController
@RequestMapping("/api/formapagamento")
public class FormaPagamentoController {

	@Getter
	@Autowired
	private FormaPagamentoService service;

	@GetMapping
	public ResponseEntity<List<FormaPagamento>> findAll(HttpServletRequest request) {
		return RestControllerUtil.findAllCompleto(service);
	}
}
