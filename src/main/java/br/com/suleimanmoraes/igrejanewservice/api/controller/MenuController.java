package br.com.suleimanmoraes.igrejanewservice.api.controller;

import java.util.List;

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
import br.com.suleimanmoraes.igrejanewservice.api.model.Menu;
import br.com.suleimanmoraes.igrejanewservice.api.service.MenuService;
import br.com.suleimanmoraes.igrejanewservice.api.util.RestControllerUtil;
import lombok.Getter;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

	@Getter
	@Autowired
	private MenuService service;

	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@PostMapping
	public ResponseEntity<Menu> newObject(HttpServletRequest request, @RequestBody Menu objeto) throws Exception {
		return RestControllerUtil.saveCompleto(getService(), objeto);
	}

	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@PutMapping
	public ResponseEntity<Menu> update(HttpServletRequest request, @RequestBody Menu objeto) throws Exception {
		return RestControllerUtil.updateCompleto(getService(), objeto);
	}

	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@PostMapping(value = "/findbyparamssingle")
	public ResponseEntity<Page<?>> findByParams(HttpServletRequest request, @RequestBody FilterDto filter) {
		return RestControllerUtil.findByParams(service, filter);
	}

	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@GetMapping(value = "{id}")
	public ResponseEntity<Menu> findById(HttpServletRequest request, @PathVariable("id") long id) {
		return RestControllerUtil.findByIdCompleto(getService(), id);
	}
	
	@GetMapping
	public ResponseEntity<List<Menu>> findAll(HttpServletRequest request) {
		return RestControllerUtil.findAllCompleto(service);
	}

	@GetMapping(value = "/ativo")
	public ResponseEntity<List<Menu>> findBy(HttpServletRequest request) {
		List<Menu> listObject = service.findBy();
		return ResponseEntity.ok(listObject);
	}
}
