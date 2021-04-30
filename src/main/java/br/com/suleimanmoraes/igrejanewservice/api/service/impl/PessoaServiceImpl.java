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

import br.com.suleimanmoraes.igrejanewservice.api.dto.ObjetoComIdDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.PessoaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterPessoaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.listagem.PessoaListagemDto;
import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.exception.NaoAutorizadoException;
import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;
import br.com.suleimanmoraes.igrejanewservice.api.model.Cargo;
import br.com.suleimanmoraes.igrejanewservice.api.model.Igreja;
import br.com.suleimanmoraes.igrejanewservice.api.model.Pessoa;
import br.com.suleimanmoraes.igrejanewservice.api.model.Usuario;
import br.com.suleimanmoraes.igrejanewservice.api.repository.PessoaRepository;
import br.com.suleimanmoraes.igrejanewservice.api.repository.dao.PessoaDao;
import br.com.suleimanmoraes.igrejanewservice.api.service.EnderecoService;
import br.com.suleimanmoraes.igrejanewservice.api.service.IgrejaService;
import br.com.suleimanmoraes.igrejanewservice.api.service.PessoaService;
import br.com.suleimanmoraes.igrejanewservice.api.service.UsuarioService;
import br.com.suleimanmoraes.igrejanewservice.api.util.RolesUtil;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@Service
public class PessoaServiceImpl implements PessoaService {

	private static final Log logger = LogFactory.getLog(PessoaService.class);

	@Getter
	@Autowired
	private PessoaRepository repository;

	@Autowired
	private PessoaDao dao;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private IgrejaService igrejaService;

	@Autowired
	private EnderecoService enderecoService;

	@Transactional(readOnly = true)
	private PessoaDto tratar(Pessoa pessoa) {
		try {
			PessoaDto dto = new PessoaDto();
			dto.setId(pessoa.getId());
			dto.setNome(pessoa.getNome());
			dto.setTelefone(pessoa.getTelefone());
			dto.setEmail(pessoa.getEmail());
			dto.setCpf(pessoa.getCpf());
			dto.setCidade(pessoa.getCidade());
			dto.setNascimento(pessoa.getNascimento());
			dto.setEstado(pessoa.getEstado());
			if (pessoa.getIgreja() != null) {
				dto.setIgreja(new ObjetoComIdDto(pessoa.getIgreja().getId()));
			}
			if (pessoa.getCargo() != null) {
				dto.setCargo(new ObjetoComIdDto(pessoa.getCargo().getId()));
			}
			if (pessoa.getEndereco() != null) {
				dto.setEndereco(enderecoService.tratar(pessoa.getEndereco()));
			}
			if (pessoa.getUsuario() != null) {
				dto.setUsuario(usuarioService.tratar(pessoa.getUsuario()));
			}
			return dto;
		} catch (Exception e) {
			logger.warn("tratar " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}

	@Override
	public void preSave(Pessoa objeto) {
		if (objeto.getEstado() == null || objeto.getEstado().getId() == null) {
			objeto.setEstado(null);
		}
		saveUsuarioAlteracaoAndCadastro(objeto, usuarioService);
	}

	@Override
	public Pessoa saveNew(String nome) {
		try {
			if (!StringUtils.hasText(nome)) {
				ValidacaoComumUtil.validarString(nome, "nome", 'o', 100);
			}
			Igreja igreja = igrejaService.findByToken();
			if (igreja == null || igreja.getId() == null) {
				igreja = new Igreja(1l);
			}
			Pessoa pessoa = new Pessoa(nome, igreja, new Cargo(2l));
			pessoa = save(pessoa);
			return pessoa;
		} catch (Exception e) {
			logger.warn("saveNew " + ExceptionUtils.getRootCauseMessage(e));
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@Override
	public void posSave(Pessoa objeto) throws Exception {
		if (objeto.getUsuario() == null || objeto.getUsuario().getId() == null) {
			new Thread(() -> {
				try {
					if (!usuarioService.existsByPessoaId(objeto.getId())) {
						objeto.setUsuario(usuarioService.createNewByPessoa(objeto));
						save(objeto);
					}
				} catch (Exception e) {
					logger.warn("posSave " + ExceptionUtils.getRootCauseMessage(e));
				}
			}).start();
		}
	}

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public void vericarMe(Long id) {
		try {
			if (id != null) {
				ObjetoComIdDto dto = usuarioService.findByToken();
				if (repository.existsByIdAndUsuarioId(id, dto.getId())) {
					return;
				}
			}
			throw new NaoAutorizadoException();
		} catch (Exception e) {
			logger.warn("vericarMe " + ExceptionUtils.getRootCauseMessage(e));
			throw new NaoAutorizadoException();
		}
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

	@Transactional
	@Override
	public PessoaDto salvar(PessoaDto objeto) {
		try {
			Long cargoId = 2l;
			Long igrejaId = null;
			Pessoa pessoa = new Pessoa();
			if (objeto.getEstado() == null || objeto.getEstado().getId() == null) {
				objeto.setEstado(null);
			}
			if (objeto.getId() != null) {
				final Pessoa p = findById(objeto.getId());
				if (p != null) {
					igrejaId = p.getIgreja().getId();
					cargoId = p.getCargo().getId();
					pessoa = p;
				} else {
					throw new NegocioException("Pessoa inválida");
				}
			}
			pessoa.setPessoa(objeto);
			if (!RolesUtil.isRoot() || pessoa.getIgreja() == null || pessoa.getIgreja().getId() == null) {
				if (igrejaId == null) {
					igrejaId = igrejaService.findByToken().getId();
				}
				pessoa.setIgreja(new Igreja(igrejaId));
			}
			if (!RolesUtil.isAdmin() || pessoa.getCargo() == null || pessoa.getCargo().getId() == null) {
				pessoa.setCargo(new Cargo(cargoId));
			}

			pessoa.setEndereco(enderecoService.tratarEnderecoDtoPreSavePessoa(objeto.getEndereco(), objeto.getId(),
					pessoa.getIgreja().getId()));
			pessoa.setUsuario(usuarioService.tratarUsuarioLsDtoPreSave(objeto.getUsuario(), objeto.getId(),
					pessoa.getIgreja().getId(), pessoa.getCargo().getId()));

			pessoa = save(pessoa);
			return tratar(pessoa);
		} catch (Exception e) {
			logger.warn("salvar " + ExceptionUtils.getRootCauseMessage(e));
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@Override
	public void validar(PessoaDto objeto) throws Exception {
		List<String> erros = new LinkedList<>();
		erros = ValidacaoComumUtil.validarString(objeto.getNome(), "Nome", 'o', erros, 100);
		erros = ValidacaoComumUtil.validarString(objeto.getCidade(), "Cidade", erros, 100);
		erros = ValidacaoComumUtil.validarString(objeto.getEmail(), "E-mail", erros, 100);
		erros = ValidacaoComumUtil.validarString(objeto.getCpf(), "CPF", erros, 11);
		erros = ValidacaoComumUtil.validarString(objeto.getTelefone(), "Telefone", erros, 11);
		erros = usuarioService.validar(objeto.getUsuario(), erros);
		Long id = 0l;
		if (objeto.getId() != null) {
			id = objeto.getId();
		}
		if (objeto.getCpf() != null && repository.existsByCpfAndIdNot(objeto.getCpf(), id)) {
			erros.add("CPF em uso, consulte o administrador.");
		}
		if (objeto.getEndereco() != null && objeto.getEndereco().isEnderecoInformado()) {
			erros = enderecoService.validar(objeto.getEndereco(), erros);
		}
		if (!CollectionUtils.isEmpty(erros)) {
			throw new NegocioException(erros);
		}
	}

	@Override
	public PessoaDto findDtoById(Long id) {
		try {
			return tratar(findById(id));
		} catch (Exception e) {
			logger.warn("findDtoById " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}

	@Override
	public PessoaDto findDtoById() {
		try {
			return tratar(repository.findTopByUsuarioId(usuarioService.findByToken().getId()));
		} catch (Exception e) {
			logger.warn("findDtoById " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}

	@Override
	public Page<PessoaListagemDto> findByParams(FilterPessoaDto filter) {
		final Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
		try {
			if (!RolesUtil.isRoot()) {
				filter.setIgrejaId(igrejaService.findByToken().getId());
			}
			final List<PessoaListagemDto> lista = dao.findByFilter(filter);
			final Integer total = dao.countByFilter(filter);
			return new PageImpl<>(lista, pageable, total);
		} catch (Exception e) {
			logger.warn("findByParams " + ExceptionUtils.getRootCauseMessage(e));
		}
		return new PageImpl<>(new LinkedList<>(), pageable, 0);
	}

	@Override
	public Boolean ativar(Long id) throws Exception {
		try {
			Pessoa objeto = findById(id);
			objeto.setAtivo(AtivoInativoEnum.ATIVO);
			objeto.getUsuario().setAtivo(AtivoInativoEnum.ATIVO);
			usuarioService.save(objeto.getUsuario());
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
			Pessoa objeto = findById(id);
			objeto.setAtivo(AtivoInativoEnum.INATIVO);
			objeto.getUsuario().setAtivo(AtivoInativoEnum.INATIVO);
			usuarioService.save(objeto.getUsuario());
			save(objeto);
			return Boolean.TRUE;
		} catch (Exception e) {
			logger.warn("deleteById " + ExceptionUtils.getRootCauseMessage(e));
			throw e;
		}
	}

	@Override
	public List<Pessoa> findByIgrejaIdAndAtivo(Long igrejaId) {
		try {
			if (!RolesUtil.isRoot()) {
				igrejaId = igrejaService.findByToken().getId();
			}
			final List<Pessoa> pessoas = repository.findByIgrejaIdAndAtivo(igrejaId, AtivoInativoEnum.ATIVO);
			return pessoas;
		} catch (Exception e) {
			logger.warn("findByIgrejaIdAndAtivo " + ExceptionUtils.getRootCauseMessage(e));
		}
		return null;
	}
}
