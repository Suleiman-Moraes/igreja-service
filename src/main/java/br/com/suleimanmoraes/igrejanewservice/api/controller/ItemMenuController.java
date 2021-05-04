package br.com.suleimanmoraes.igrejanewservice.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterDto;
import br.com.suleimanmoraes.igrejanewservice.api.model.ItemMenu;
import br.com.suleimanmoraes.igrejanewservice.api.service.ItemMenuService;
import br.com.suleimanmoraes.igrejanewservice.api.util.RestControllerUtil;
import lombok.Getter;

@RestController
@RequestMapping("/api/itemmenu")
public class ItemMenuController {

	@Getter
	@Autowired
	private ItemMenuService service;

	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@PostMapping
	public ResponseEntity<ItemMenu> newObject(HttpServletRequest request, @RequestBody ItemMenu objeto) throws Exception {
		return RestControllerUtil.saveCompleto(getService(), objeto);
	}

	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@PutMapping
	public ResponseEntity<ItemMenu> update(HttpServletRequest request, @RequestBody ItemMenu objeto) throws Exception {
		return RestControllerUtil.updateCompleto(getService(), objeto);
	}

	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@PostMapping(value = "/findbyparamssingle")
	public ResponseEntity<Page<?>> findByParams(HttpServletRequest request, @RequestBody FilterDto filter) {
		return RestControllerUtil.findByParams(service, filter);
	}

	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@GetMapping(value = "{id}")
	public ResponseEntity<ItemMenu> findById(HttpServletRequest request, @PathVariable("id") long id) {
		return RestControllerUtil.findByIdCompleto(getService(), id);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@DeleteMapping(value = "{id}")
	public ResponseEntity<Boolean> deleteById(HttpServletRequest request, @PathVariable("id") long id)
			throws Exception {
		return RestControllerUtil.deleteByIdCompleto(getService(), id);
	}

	@PreAuthorize("hasAuthority('ROLE_ROOT')")
	@PutMapping(value = "/ativar/{id}")
	public ResponseEntity<Boolean> ativar(HttpServletRequest request, @PathVariable("id") long id) throws Exception {
		Boolean retorno = service.ativar(id);
		return ResponseEntity.ok(retorno);
	}
}
