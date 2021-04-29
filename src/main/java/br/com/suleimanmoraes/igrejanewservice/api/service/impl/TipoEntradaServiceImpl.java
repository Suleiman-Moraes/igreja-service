package br.com.suleimanmoraes.igrejanewservice.api.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;
import br.com.suleimanmoraes.igrejanewservice.api.model.TipoEntrada;
import br.com.suleimanmoraes.igrejanewservice.api.repository.TipoEntradaRepository;
import br.com.suleimanmoraes.igrejanewservice.api.service.TipoEntradaService;
import br.com.suleimanmoraes.igrejanewservice.api.service.UsuarioService;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@Service
public class TipoEntradaServiceImpl implements TipoEntradaService {

	private static final Log logger = LogFactory.getLog(TipoEntradaService.class);

	@Getter
	@Autowired
	private TipoEntradaRepository repository;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public void preSave(TipoEntrada objeto) {
		saveUsuarioAlteracaoAndCadastro(objeto, usuarioService);
	}

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public void validar(TipoEntrada objeto) throws NegocioException {
		List<String> erros = new LinkedList<>();
		erros = ValidacaoComumUtil.validarString(objeto.getNome(), "Nome", 'o', erros, 100);
		erros = ValidacaoComumUtil.validarString(objeto.getDescricao(), "Descrição", erros, 300);
		if (!CollectionUtils.isEmpty(erros)) {
			throw new NegocioException(erros);
		}
	}

	@Override
	public Boolean ativar(Long id) throws Exception {
		try {
			TipoEntrada objeto = findById(id);
			objeto.setAtivo(AtivoInativoEnum.ATIVO);
			save(objeto);
			return Boolean.TRUE;
		} catch (Exception e) {
			logger.warn("ativar " + ExceptionUtils.getRootCauseMessage(e));
			throw e;
		}
	}

	@Override
	public Boolean deleteById(Long id) throws Exception {
		try {
			TipoEntrada objeto = findById(id);
			objeto.setAtivo(AtivoInativoEnum.INATIVO);
			save(objeto);
			return Boolean.TRUE;
		} catch (Exception e) {
			logger.warn("deleteById " + ExceptionUtils.getRootCauseMessage(e));
			throw e;
		}
	}
	
	@Override
	public List<TipoEntrada> findAll() {
		try {
			return repository.findByAtivoOrderByNomeAsc(AtivoInativoEnum.ATIVO);
		} catch (Exception e) {
			logger.warn("findAll " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}
}
