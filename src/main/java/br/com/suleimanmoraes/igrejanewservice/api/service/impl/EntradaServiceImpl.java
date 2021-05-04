package br.com.suleimanmoraes.igrejanewservice.api.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import br.com.suleimanmoraes.igrejanewservice.api.dto.EntradaInformacaoDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.GraficoDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.GraficoGroupDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterEntradaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.listagem.EntradaListagemDto;
import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.enums.OpcaoTratarEnum;
import br.com.suleimanmoraes.igrejanewservice.api.exception.NaoAutorizadoException;
import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;
import br.com.suleimanmoraes.igrejanewservice.api.model.Entrada;
import br.com.suleimanmoraes.igrejanewservice.api.model.FormaPagamento;
import br.com.suleimanmoraes.igrejanewservice.api.model.Igreja;
import br.com.suleimanmoraes.igrejanewservice.api.model.Pessoa;
import br.com.suleimanmoraes.igrejanewservice.api.model.TipoEntrada;
import br.com.suleimanmoraes.igrejanewservice.api.model.Usuario;
import br.com.suleimanmoraes.igrejanewservice.api.repository.EntradaRepository;
import br.com.suleimanmoraes.igrejanewservice.api.repository.dao.EntradaDao;
import br.com.suleimanmoraes.igrejanewservice.api.service.EntradaService;
import br.com.suleimanmoraes.igrejanewservice.api.service.IgrejaService;
import br.com.suleimanmoraes.igrejanewservice.api.service.PessoaService;
import br.com.suleimanmoraes.igrejanewservice.api.service.UsuarioService;
import br.com.suleimanmoraes.igrejanewservice.api.util.RolesUtil;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@Service
public class EntradaServiceImpl implements EntradaService {

	private static final Log logger = LogFactory.getLog(EntradaService.class);

	@Getter
	@Autowired
	private EntradaRepository repository;

	@Autowired
	private EntradaDao dao;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private IgrejaService igrejaService;

	@Override
	public void preSave(Entrada objeto) {
		saveUsuarioAlteracaoAndCadastro(objeto, usuarioService);
		if (!RolesUtil.isRoot() || objeto.getIgreja() == null || objeto.getIgreja().getId() == null) {
			objeto.setIgreja(new Igreja(igrejaService.findByToken().getId()));
		}
		if (objeto.getFormaPagamento() == null || objeto.getFormaPagamento().getId() == null) {
			objeto.setFormaPagamento(null);
		}
		if (objeto.getTipoEntrada() == null || objeto.getTipoEntrada().getId() == null) {
			objeto.setTipoEntrada(null);
		}
		if (objeto.getPessoa() != null) {
			if (objeto.getPessoa().getId() == null) {
				if (StringUtils.hasText(objeto.getPessoa().getNome())) {
					objeto.setPessoa(pessoaService.saveNew(objeto.getPessoa().getNome()));
				} else {
					objeto.setPessoa(null);
				}
			}
		}
	}

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public void validar(Entrada objeto) throws Exception {
		List<String> erros = new LinkedList<>();
		erros = ValidacaoComumUtil.validarString(objeto.getNome(), "Nome", 'o', erros, 100);
		erros = ValidacaoComumUtil.validarString(objeto.getDescricao(), "Descrição", erros, 300);
		erros = ValidacaoComumUtil.validarNotNull(objeto.getDataEntrada(), "Data", 'a', erros);
		erros = ValidacaoComumUtil.validarNotNullAndMaiorZero(objeto.getValor(), "Valor", 'o', erros);
		erros = ValidacaoComumUtil.validarObjectAndId(objeto.getTipoEntrada(), "Tipo da Entrada", 'o', erros);
		if (!CollectionUtils.isEmpty(erros)) {
			throw new NegocioException(erros);
		}
	}

	@Override
	public Entrada tratar(Entrada objeto, OpcaoTratarEnum opcao) {
		if (objeto != null) {
			if (OpcaoTratarEnum.MOSTRAR.equals(opcao)) {
				objeto.setIgreja(objeto.getIgreja() == null ? null : new Igreja(objeto.getIgreja().getId()));
				objeto.setTipoEntrada(
						objeto.getTipoEntrada() == null ? null : new TipoEntrada(objeto.getTipoEntrada().getId()));
				objeto.setPessoa(objeto.getPessoa() == null ? null : new Pessoa(objeto.getPessoa().getId()));
				objeto.setFormaPagamento(objeto.getFormaPagamento() == null ? null
						: new FormaPagamento(objeto.getFormaPagamento().getId()));
			}
		}
		return objeto;
	}

	@Override
	public Boolean ativar(Long id) throws Exception {
		try {
			Entrada objeto = findById(id);
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
			Entrada objeto = findById(id);
			objeto.setAtivo(AtivoInativoEnum.INATIVO);
			save(objeto);
			return Boolean.TRUE;
		} catch (Exception e) {
			logger.warn("deleteById " + ExceptionUtils.getRootCauseMessage(e));
			throw e;
		}
	}

	@Override
	public Page<EntradaListagemDto> findByParams(FilterEntradaDto filter) {
		final Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
		try {
			if (!RolesUtil.isRoot()) {
				filter.setIgrejaId(igrejaService.findByToken().getId());
			}
			final List<EntradaListagemDto> lista = dao.findByFilter(filter);
			final Integer total = dao.countByFilter(filter);
			return new PageImpl<>(lista, pageable, total);
		} catch (Exception e) {
			logger.warn("findByParams " + ExceptionUtils.getRootCauseMessage(e));
		}
		return new PageImpl<>(new LinkedList<>(), pageable, 0);
	}

	@Override
	public GraficoDto montarGraficoAnual(Integer ano) {
		GraficoDto grafico = new GraficoDto("Entradas", "green");
		try {
			final List<GraficoGroupDto> valores = dao.somaValorMensalPorAno(ano);
			Integer[] v = new Integer[12];
			if (!CollectionUtils.isEmpty(valores)) {
				valores.forEach(valor -> v[valor.getMes() - 1] = valor.getValor().intValue());
			}
			grafico.setData(v);
		} catch (Exception e) {
			logger.warn("montarGraficoAnual " + ExceptionUtils.getRootCauseMessage(e));
		}
		return grafico;
	}

	@Override
	public EntradaInformacaoDto getInformacao(FilterEntradaDto filter) {
		try {
			if (!RolesUtil.isRoot()) {
				filter.setIgrejaId(igrejaService.findByToken().getId());
			}
			filter.setAtivo(AtivoInativoEnum.ATIVO);
			filter.setTipoEntradaId(null);
			EntradaInformacaoDto info = dao.getInformacao(filter);
			info.setFiltro(filter);
			if (info.getTotalRegistros() > 0) {
				info.setTipoEntradaInfomacoes(dao.getTipoEntradaInformacoes(filter));
			}
			return info;
		} catch (Exception e) {
			logger.warn("getInformacao " + ExceptionUtils.getRootCauseMessage(e));
		}
		return new EntradaInformacaoDto();
	}

	@Override
	public void vericarIgreja(Long id) {
		try {
			if (id != null) {
				Usuario usuario = usuarioService.findByLogin();
				if (RolesUtil.isRoot() || repository.existsByIdAndIgrejaId(id, usuario.getIgreja().getId())) {
					return;
				}
			} else {
				return;
			}
			throw new NaoAutorizadoException();
		} catch (NaoAutorizadoException e) {
			logger.warn("vericarMe " + ExceptionUtils.getRootCauseMessage(e));
			throw e;
		} catch (Exception e) {
			logger.warn("vericarMe " + ExceptionUtils.getRootCauseMessage(e));
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public Entrada inserirDizimo(Long pessoaId, Double valor) {
		try {
			if (pessoaId > 0) {
				ValidacaoComumUtil.validarNotNullAndMaiorZero(valor, "Valor", 'o');
				final Long igrejaId = igrejaService.findByToken().getId();
				final Pessoa pessoa = pessoaService.findByIdAndIgrejaId(pessoaId, igrejaId);
				if (pessoa == null || pessoa.getId() == null) {
					throw new NegocioException("Pessoa não encontrada.");
				}
				Entrada entrada = new Entrada(valor, String.format("Devolução de Dízimo do \"%s\".", pessoa.getNome()),
						new TipoEntrada(1l), pessoa);
				entrada = save(entrada);
				return entrada;
			}
		} catch (Exception e) {
			logger.warn("inserirDizimo " + ExceptionUtils.getRootCauseMessage(e));
			throw new NegocioException(e.getMessage(), e);
		}
		return null;
	}
}
