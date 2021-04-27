package br.com.suleimanmoraes.igrejanewservice.api.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;
import br.com.suleimanmoraes.igrejanewservice.api.model.FormaPagamento;
import br.com.suleimanmoraes.igrejanewservice.api.repository.FormaPagamentoRepository;
import br.com.suleimanmoraes.igrejanewservice.api.service.FormaPagamentoService;
import br.com.suleimanmoraes.igrejanewservice.api.service.UsuarioService;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@Service
public class FormaPagamentoServiceImpl implements FormaPagamentoService {

	private static final Log logger = LogFactory.getLog(FormaPagamentoService.class);

	@Getter
	@Autowired
	private FormaPagamentoRepository repository;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public void preSave(FormaPagamento objeto) {
		saveUsuarioAlteracaoAndCadastro(objeto, usuarioService);
	}

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public void validar(FormaPagamento objeto) throws NegocioException {
		List<String> erros = new LinkedList<>();
		erros = ValidacaoComumUtil.validarString(objeto.getNome(), "Nome", 'o', erros, 100);
		erros = ValidacaoComumUtil.validarString(objeto.getDescricao(), "Descrição", erros, 300);
		if (!CollectionUtils.isEmpty(erros)) {
			throw new NegocioException(erros);
		}
	}
}
