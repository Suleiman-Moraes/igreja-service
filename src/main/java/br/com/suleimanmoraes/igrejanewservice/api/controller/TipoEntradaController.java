package br.com.suleimanmoraes.igrejanewservice.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.suleimanmoraes.igrejanewservice.api.model.TipoEntrada;
import br.com.suleimanmoraes.igrejanewservice.api.service.TipoEntradaService;
import br.com.suleimanmoraes.igrejanewservice.api.util.RestControllerUtil;
import lombok.Getter;

@RestController
@RequestMapping("/api/tipoentrada")
public class TipoEntradaController {

	@Getter
	@Autowired
	private TipoEntradaService service;
	
	@GetMapping
	public ResponseEntity<List<TipoEntrada>> findAll(HttpServletRequest request) {
		return RestControllerUtil.findAllCompleto(service);
	}

//	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
//	@PostMapping
//	public ResponseEntity<TipoEntrada> newObject(HttpServletRequest request, @RequestBody TipoEntrada objeto) throws Exception {
//		return RestControllerUtil.saveCompleto(getService(), objeto);
//	}
//
//	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
//	@PutMapping
//	public ResponseEntity<TipoEntrada> update(HttpServletRequest request, @RequestBody TipoEntrada objeto) throws Exception {
//		return RestControllerUtil.updateCompleto(getService(), objeto);
//	}
//
//	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
//	@PostMapping(value = "/params")
//	public ResponseEntity<Page<?>> findByParams(HttpServletRequest request,
//			@RequestBody FilterDto filter) {
//		return RestControllerUtil.findByParams(service, filter);
//	}
//
//	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
//	@GetMapping(value = "{id}")
//	public ResponseEntity<TipoEntrada> findById(HttpServletRequest request, @PathVariable("id") long id) {
//		return RestControllerUtil.findByIdCompleto(getService(), id);
//	}
//
//	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
//	@DeleteMapping(value = "{id}")
//	public ResponseEntity<Boolean> deleteById(HttpServletRequest request, @PathVariable("id") long id)
//			throws Exception {
//		return RestControllerUtil.deleteByIdCompleto(getService(), id);
//	}
//
//	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
//	@PutMapping(value = "/ativar/{id}")
//	public ResponseEntity<Boolean> ativar(HttpServletRequest request, @PathVariable("id") long id) throws Exception {
//		Boolean retorno = service.ativar(id);
//		return ResponseEntity.ok(retorno);
//	}
}
