package br.com.suleimanmoraes.igrejanewservice.api.service.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.suleimanmoraes.igrejanewservice.api.dto.EnderecoDto;
import br.com.suleimanmoraes.igrejanewservice.api.model.Endereco;
import br.com.suleimanmoraes.igrejanewservice.api.repository.EnderecoRepository;
import br.com.suleimanmoraes.igrejanewservice.api.service.EnderecoService;
import br.com.suleimanmoraes.igrejanewservice.api.service.UsuarioService;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@Service
public class EnderecoServiceImpl implements EnderecoService {

	private static final Log logger = LogFactory.getLog(EnderecoService.class);

	@Getter
	@Autowired
	private EnderecoRepository repository;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public void preSave(Endereco objeto) {
		saveUsuarioAlteracaoAndCadastro(objeto, usuarioService);
	}

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public List<String> validar(EnderecoDto objeto, List<String> erros) throws Exception {
		if (objeto != null) {
			erros = ValidacaoComumUtil.validarString(objeto.getCep(), "Cep", 'o', erros, 8);
			erros = ValidacaoComumUtil.validarString(objeto.getBairro(), "Bairro", 'o', erros, 100);
			erros = ValidacaoComumUtil.validarString(objeto.getRua(), "Rua", 'a', erros, 100);
			erros = ValidacaoComumUtil.validarString(objeto.getCidade(), "Cidade", 'a', erros, 100);
			erros = ValidacaoComumUtil.validarObjectAndId(objeto.getEstado(), "Estado", 'o', erros);
			erros = ValidacaoComumUtil.validarString(objeto.getComplemento(), "Complemento", erros, 300);
			erros = ValidacaoComumUtil.validarString(objeto.getLote(), "Lote", erros, 20);
			erros = ValidacaoComumUtil.validarString(objeto.getQuadra(), "Quadra", erros, 20);
			erros = ValidacaoComumUtil.validarString(objeto.getNumero(), "Número", erros, 10);
		}
		return erros;
	}

	@Override
	public EnderecoDto tratar(Endereco endereco) {
		final EnderecoDto dto = new EnderecoDto(endereco);
		return dto;
	}

	@Override
	public Endereco tratarEnderecoDtoPreSavePessoa(EnderecoDto objeto, Long pessoaId, Long igrejaId) {
		Endereco endereco = null;

		if (objeto != null && objeto.isEnderecoInformado()) {
			endereco = new Endereco(objeto);
			if (pessoaId == null) {
				endereco.setId(null);
			} else {
				Endereco aux = findByPessoasId(pessoaId);
				endereco.setId(aux.getId());
				endereco.setIdUsuarioCadastro(aux.getIdUsuarioCadastro());
				endereco.setDataCadastro(aux.getDataCadastro());
			}
			endereco = save(endereco);
		} else {
			if (pessoaId != null) {
				return findByPessoasId(pessoaId);
			}
		}
		return endereco;
	}

	@Transactional(readOnly = true)
	@Override
	public Endereco findByPessoasId(Long pessoaId) {
		try {
			return repository.findTopByPessoasId(pessoaId);
		} catch (Exception e) {
			logger.warn("findByPessoasId " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}
}
