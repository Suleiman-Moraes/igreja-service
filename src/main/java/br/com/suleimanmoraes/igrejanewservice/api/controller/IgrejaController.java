package br.com.suleimanmoraes.igrejanewservice.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.suleimanmoraes.igrejanewservice.api.model.Igreja;
import br.com.suleimanmoraes.igrejanewservice.api.service.IgrejaService;
import br.com.suleimanmoraes.igrejanewservice.api.util.RestControllerUtil;
import lombok.Getter;

@RestController
@RequestMapping("/api/igreja")
public class IgrejaController {

	@Getter
	@Autowired
	private IgrejaService service;

	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@GetMapping
	public ResponseEntity<List<Igreja>> findAll(HttpServletRequest request) {
		return RestControllerUtil.findAllCompleto(service);
	}
}
