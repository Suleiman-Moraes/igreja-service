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

import br.com.suleimanmoraes.igrejanewservice.api.dto.EntradaInformacaoDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterEntradaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.listagem.EntradaListagemDto;
import br.com.suleimanmoraes.igrejanewservice.api.model.Entrada;
import br.com.suleimanmoraes.igrejanewservice.api.service.EntradaService;
import br.com.suleimanmoraes.igrejanewservice.api.util.RestControllerUtil;
import lombok.Getter;

@RestController
@RequestMapping("/api/entrada")
public class EntradaController {

	@Getter
	@Autowired
	private EntradaService service;

	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
	@PostMapping
	public ResponseEntity<Entrada> newObject(HttpServletRequest request, @RequestBody Entrada objeto) throws Exception {
		return RestControllerUtil.saveCompleto(getService(), objeto);
	}

	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
	@PutMapping
	public ResponseEntity<Entrada> update(HttpServletRequest request, @RequestBody Entrada objeto) throws Exception {
		return RestControllerUtil.updateCompleto(getService(), objeto);
	}

	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
	@PostMapping(value = "/params")
	public ResponseEntity<Page<EntradaListagemDto>> findByParams(HttpServletRequest request,
			@RequestBody FilterEntradaDto filter) {
		final Page<EntradaListagemDto> pagina = service.findByParams(filter);
		return ResponseEntity.ok(pagina);
	}

	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
	@GetMapping(value = "{id}")
	public ResponseEntity<Entrada> findById(HttpServletRequest request, @PathVariable("id") long id) {
		return RestControllerUtil.findByIdCompleto(getService(), id);
	}

	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
	@DeleteMapping(value = "{id}")
	public ResponseEntity<Boolean> deleteById(HttpServletRequest request, @PathVariable("id") long id)
			throws Exception {
		return RestControllerUtil.deleteByIdCompleto(getService(), id);
	}

	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
	@PutMapping(value = "/ativar/{id}")
	public ResponseEntity<Boolean> ativar(HttpServletRequest request, @PathVariable("id") long id) throws Exception {
		Boolean retorno = service.ativar(id);
		return ResponseEntity.ok(retorno);
	}

	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
	@PostMapping(value = "/info")
	public ResponseEntity<EntradaInformacaoDto> getInformacao(HttpServletRequest request,
			@RequestBody FilterEntradaDto filter) {
		EntradaInformacaoDto retorno = service.getInformacao(filter);
		return ResponseEntity.ok(retorno);
	}
}
