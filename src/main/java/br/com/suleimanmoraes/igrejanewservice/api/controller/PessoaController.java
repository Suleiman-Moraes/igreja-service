package br.com.suleimanmoraes.igrejanewservice.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.suleimanmoraes.igrejanewservice.api.dto.PessoaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterPessoaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.listagem.PessoaListagemDto;
import br.com.suleimanmoraes.igrejanewservice.api.enums.OpcaoTratarEnum;
import br.com.suleimanmoraes.igrejanewservice.api.model.Pessoa;
import br.com.suleimanmoraes.igrejanewservice.api.service.PessoaService;
import br.com.suleimanmoraes.igrejanewservice.api.util.RestControllerUtil;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@RestController
@RequestMapping("/api/pessoa")
public class PessoaController {

	@Getter
	@Autowired
	private PessoaService service;

	@PreAuthorize("hasAuthority('ROLE_PESSOA')")
	@PostMapping
	public ResponseEntity<PessoaDto> newObject(HttpServletRequest request, @RequestBody PessoaDto objeto)
			throws Exception {
		service.vericarIgreja(objeto.getId());
		service.validar(objeto);
		PessoaDto objetoNovo = service.salvar(objeto);
		return ResponseEntity.status(HttpStatus.OK).body(objetoNovo);
	}

	@PreAuthorize("hasAuthority('ROLE_PESSOA')")
	@PostMapping(value = "/nome")
	public ResponseEntity<Pessoa> saveNew(HttpServletRequest request, @RequestParam(name = "nome") String nome) {
		Pessoa objetoNovo = service.saveNew(nome);
		objetoNovo = service.tratar(objetoNovo, OpcaoTratarEnum.MOSTRAR);
		return ResponseEntity.status(HttpStatus.OK).body(objetoNovo);
	} 

	@PreAuthorize("hasAuthority('ROLE_PESSOA')")
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

	@PreAuthorize("hasAuthority('ROLE_PESSOA')")
	@PostMapping(value = "/params")
	public ResponseEntity<Page<PessoaListagemDto>> findByParams(HttpServletRequest request,
			@RequestBody FilterPessoaDto filter) {
		final Page<PessoaListagemDto> pagina = service.findByParams(filter);
		return ResponseEntity.ok(pagina);
	}

	@PreAuthorize("hasAuthority('ROLE_PESSOA')")
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

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@DeleteMapping(value = "{id}")
	public ResponseEntity<Boolean> deleteById(HttpServletRequest request, @PathVariable("id") long id)
			throws Exception {
		return RestControllerUtil.deleteByIdCompleto(getService(), id);
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PutMapping(value = "/ativar/{id}")
	public ResponseEntity<Boolean> ativar(HttpServletRequest request, @PathVariable("id") long id) throws Exception {
		Boolean retorno = service.ativar(id);
		return ResponseEntity.ok(retorno);
	}

	@PreAuthorize("hasAuthority('ROLE_TESOUREIRO')")
	@GetMapping(value = "/igreja/{igrejaId}")
	public ResponseEntity<List<Pessoa>> findByIgrejaIdAndAtivo(HttpServletRequest request,
			@PathVariable("igrejaId") long igrejaId) {
		List<Pessoa> objetos = service.findByIgrejaIdAndAtivo(igrejaId);
		return ResponseEntity.ok(objetos);
	}
}
