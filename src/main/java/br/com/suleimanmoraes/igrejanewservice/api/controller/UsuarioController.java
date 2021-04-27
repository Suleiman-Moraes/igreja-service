package br.com.suleimanmoraes.igrejanewservice.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.suleimanmoraes.igrejanewservice.api.dto.UsuarioLogadoDto;
import br.com.suleimanmoraes.igrejanewservice.api.service.UsuarioService;
import lombok.Getter;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

	@Getter
	@Autowired
	private UsuarioService service;

	@GetMapping(value = "/me")
	public ResponseEntity<UsuarioLogadoDto> findUsuarioLogadoDtoBy(HttpServletRequest request) {
		UsuarioLogadoDto objeto = service.findUsuarioLogadoDtoBy();
		return ResponseEntity.ok(objeto);
	}
}
