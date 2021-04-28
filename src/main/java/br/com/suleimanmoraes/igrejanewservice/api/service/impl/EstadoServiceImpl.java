package br.com.suleimanmoraes.igrejanewservice.api.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;
import br.com.suleimanmoraes.igrejanewservice.api.model.Estado;
import br.com.suleimanmoraes.igrejanewservice.api.repository.EstadoRepository;
import br.com.suleimanmoraes.igrejanewservice.api.service.EstadoService;
import br.com.suleimanmoraes.igrejanewservice.api.service.UsuarioService;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@Service
public class EstadoServiceImpl implements EstadoService {

	private static final Log logger = LogFactory.getLog(EstadoService.class);

	@Getter
	@Autowired
	private EstadoRepository repository;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public void preSave(Estado objeto) {
		saveUsuarioAlteracaoAndCadastro(objeto, usuarioService);
		objeto.setUf(objeto.getUf().toUpperCase());
	}

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public void validar(Estado objeto) throws NegocioException {
		List<String> erros = new LinkedList<>();
		erros = ValidacaoComumUtil.validarString(objeto.getNome(), "Nome", 'o', erros, 100);
		erros = ValidacaoComumUtil.validarString(objeto.getUf(), "UF", 'a', erros, 2);
		if (!CollectionUtils.isEmpty(erros)) {
			throw new NegocioException(erros);
		}
	}

	@Override
	public List<Estado> findAll() {
		try {
			return repository.findByOrderByNomeAsc();
		} catch (Exception e) {
			logger.warn("findAll " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}
}
