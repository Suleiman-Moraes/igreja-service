package br.com.suleimanmoraes.igrejanewservice.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterDto;
import br.com.suleimanmoraes.igrejanewservice.api.model.Permissao;
import br.com.suleimanmoraes.igrejanewservice.api.service.PermissaoService;
import br.com.suleimanmoraes.igrejanewservice.api.util.RestControllerUtil;
import lombok.Getter;

@RestController
@RequestMapping("/api/permissao")
public class PermissaoController {

	@Getter
	@Autowired
	private PermissaoService service;

	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@PostMapping
	public ResponseEntity<Permissao> newObject(HttpServletRequest request, @RequestBody Permissao objeto) throws Exception {
		return RestControllerUtil.saveCompleto(getService(), objeto);
	}

	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@PutMapping
	public ResponseEntity<Permissao> update(HttpServletRequest request, @RequestBody Permissao objeto) throws Exception {
		return RestControllerUtil.updateCompleto(getService(), objeto);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@PostMapping(value = "/params")
	public ResponseEntity<Page<?>> findByParams(HttpServletRequest request,
			@RequestBody FilterDto filter) {
		return RestControllerUtil.findByParams(service, filter);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@GetMapping(value = "{id}")
	public ResponseEntity<Permissao> findById(HttpServletRequest request, @PathVariable("id") long id) {
		return RestControllerUtil.findByIdCompleto(getService(), id);
	}
}
