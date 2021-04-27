package br.com.suleimanmoraes.igrejanewservice.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.suleimanmoraes.igrejanewservice.api.dto.PessoaDto;
import br.com.suleimanmoraes.igrejanewservice.api.enums.OpcaoTratarEnum;
import br.com.suleimanmoraes.igrejanewservice.api.model.Pessoa;
import br.com.suleimanmoraes.igrejanewservice.api.service.PessoaService;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@RestController
@RequestMapping("/api/pessoa")
public class PessoaController {

	@Getter
	@Autowired
	private PessoaService service;

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<PessoaDto> newObject(HttpServletRequest request, @RequestBody PessoaDto objeto) throws Exception {
		service.validar(objeto);
		PessoaDto objetoNovo = service.salvar(objeto);
		return ResponseEntity.status(HttpStatus.OK).body(objetoNovo);
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PostMapping(value = "/nome")
	public ResponseEntity<Pessoa> saveNew(HttpServletRequest request, @RequestParam(name = "nome") String nome) {
		Pessoa objetoNovo = service.saveNew(nome);
		objetoNovo = service.tratar(objetoNovo, OpcaoTratarEnum.MOSTRAR);
		return ResponseEntity.status(HttpStatus.OK).body(objetoNovo);
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PutMapping
	public ResponseEntity<PessoaDto> update(HttpServletRequest request, @RequestBody PessoaDto objeto)
			throws Exception {
		ValidacaoComumUtil.validarObjectAndId(objeto, "Id", 'o');
		service.vericarIgreja(objeto.getId());
		service.validar(objeto);
		PessoaDto objetoNovo = service.salvar(objeto);
		return ResponseEntity.status(HttpStatus.OK).body(objetoNovo);
	}

	@PutMapping(value = "me")
	public ResponseEntity<PessoaDto> updateMe(HttpServletRequest request, @RequestBody PessoaDto objeto)
			throws Exception {
		ValidacaoComumUtil.validarObjectAndId(objeto, "Id", 'o');
		service.vericarMe(objeto.getId());
		service.validar(objeto);
		PessoaDto objetoNovo = service.salvar(objeto);
		return ResponseEntity.status(HttpStatus.OK).body(objetoNovo);
	}

	// @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	// @PostMapping(value = "/params")
	// public ResponseEntity<Page<?>> findByParams(HttpServletRequest request,
	// @RequestBody FilterDto filter) {
	// return RestControllerUtil.findByParams(service, filter);
	// }

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping(value = "{id}")
	public ResponseEntity<PessoaDto> findById(HttpServletRequest request, @PathVariable("id") long id) {
		PessoaDto objeto = service.findDtoById(id);
		return ResponseEntity.ok(objeto);
	}

	@GetMapping(value = "/me/id")
	public ResponseEntity<PessoaDto> findByIdMe(HttpServletRequest request) {
		PessoaDto objeto = service.findDtoById();
		return ResponseEntity.ok(objeto);
	}
}
