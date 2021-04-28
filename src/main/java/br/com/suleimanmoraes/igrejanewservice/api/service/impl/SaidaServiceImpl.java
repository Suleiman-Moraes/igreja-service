package br.com.suleimanmoraes.igrejanewservice.api.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterSaidaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.listagem.SaidaListagemDto;
import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.enums.OpcaoTratarEnum;
import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;
import br.com.suleimanmoraes.igrejanewservice.api.model.FormaPagamento;
import br.com.suleimanmoraes.igrejanewservice.api.model.Igreja;
import br.com.suleimanmoraes.igrejanewservice.api.model.Saida;
import br.com.suleimanmoraes.igrejanewservice.api.model.SaidaProgramada;
import br.com.suleimanmoraes.igrejanewservice.api.repository.SaidaRepository;
import br.com.suleimanmoraes.igrejanewservice.api.repository.dao.SaidaDao;
import br.com.suleimanmoraes.igrejanewservice.api.service.IgrejaService;
import br.com.suleimanmoraes.igrejanewservice.api.service.SaidaService;
import br.com.suleimanmoraes.igrejanewservice.api.service.UsuarioService;
import br.com.suleimanmoraes.igrejanewservice.api.util.RolesUtil;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@Service
public class SaidaServiceImpl implements SaidaService {

	private static final Log logger = LogFactory.getLog(SaidaService.class);

	@Getter
	@Autowired
	private SaidaRepository repository;

	@Autowired
	private SaidaDao dao;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private IgrejaService igrejaService;

	@Override
	public void preSave(Saida objeto) {
		saveUsuarioAlteracaoAndCadastro(objeto, usuarioService);
		if (!RolesUtil.isRoot() || objeto.getIgreja() == null || objeto.getIgreja().getId() == null) {
			objeto.setIgreja(new Igreja(igrejaService.findByToken().getId()));
		}
	}

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public void validar(Saida objeto) throws NegocioException {
		List<String> erros = new LinkedList<>();
		erros = ValidacaoComumUtil.validarString(objeto.getNome(), "Nome", 'o', erros, 100);
		erros = ValidacaoComumUtil.validarString(objeto.getDescricao(), "Descrição", erros, 300);
		erros = ValidacaoComumUtil.validarNotNull(objeto.getDataSaida(), "Data", 'a', erros);
		erros = ValidacaoComumUtil.validarNotNullAndMaiorZero(objeto.getValor(), "Valor", 'o', erros);
		if (!CollectionUtils.isEmpty(erros)) {
			throw new NegocioException(erros);
		}
	}

	@Override
	public Saida tratar(Saida objeto, OpcaoTratarEnum opcao) {
		if (objeto != null) {
			if (OpcaoTratarEnum.MOSTRAR.equals(opcao)) {
				objeto.setIgreja(objeto.getIgreja() == null ? null : new Igreja(objeto.getIgreja().getId()));
				objeto.setSaidaProgramada(objeto.getSaidaProgramada() == null ? null
						: new SaidaProgramada(objeto.getSaidaProgramada().getId()));
				objeto.setFormaPagamento(objeto.getFormaPagamento() == null ? null
						: new FormaPagamento(objeto.getFormaPagamento().getId()));
			}
		}
		return objeto;
	}

	@Override
	public Boolean ativar(Long id) throws Exception {
		try {
			Saida objeto = findById(id);
			objeto.setAtivo(AtivoInativoEnum.ATIVO);
			save(objeto);
			return Boolean.TRUE;
		} catch (Exception e) {
			logger.warn("ativar " + e.getMessage());
			throw e;
		}
	}

	@Override
	public Boolean deleteById(Long id) throws Exception {
		try {
			Saida objeto = findById(id);
			objeto.setAtivo(AtivoInativoEnum.INATIVO);
			save(objeto);
			return Boolean.TRUE;
		} catch (Exception e) {
			logger.warn("deleteById " + e.getMessage());
			throw e;
		}
	}

	@Override
	public Page<SaidaListagemDto> findByParams(FilterSaidaDto filter) {
		final Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
		try {
			if (!RolesUtil.isRoot()) {
				filter.setIgrejaId(igrejaService.findByToken().getId());
			}
			final List<SaidaListagemDto> lista = dao.findByFilter(filter);
			final Integer total = dao.countByFilter(filter);
			return new PageImpl<>(lista, pageable, total);
		} catch (Exception e) {
			logger.warn("findByFilter " + e.getMessage());
		}
		return new PageImpl<>(new LinkedList<>(), pageable, 0);
	}
}
