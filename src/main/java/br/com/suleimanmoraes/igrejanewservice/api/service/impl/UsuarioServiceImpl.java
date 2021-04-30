package br.com.suleimanmoraes.igrejanewservice.api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import br.com.suleimanmoraes.igrejanewservice.api.dto.ObjetoComIdDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.UsuarioLogadoDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.UsuarioLsDto;
import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.enums.OpcaoTratarEnum;
import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;
import br.com.suleimanmoraes.igrejanewservice.api.model.Cargo;
import br.com.suleimanmoraes.igrejanewservice.api.model.Igreja;
import br.com.suleimanmoraes.igrejanewservice.api.model.Pessoa;
import br.com.suleimanmoraes.igrejanewservice.api.model.Usuario;
import br.com.suleimanmoraes.igrejanewservice.api.repository.UsuarioRepository;
import br.com.suleimanmoraes.igrejanewservice.api.service.PermissaoService;
import br.com.suleimanmoraes.igrejanewservice.api.service.UsuarioService;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	private static final Log logger = LogFactory.getLog(UsuarioService.class);

	@Getter
	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private PermissaoService permissaoService;

	private Long findIdTopByPessoaIdAndIdNot(Long pessoaId, Long id) {
		try {
			pessoaId = pessoaId == null ? 0l : pessoaId;
			id = id == null ? 0l : id;
			return repository.findIdTopByPessoaIdAndIdNot(pessoaId, id);
		} catch (Exception e) {
			logger.warn("createNewByPessoa " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}

	@Override
	public void preSave(Usuario objeto) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		saveUsuarioAlteracaoAndCadastro(objeto, this);
		if (StringUtils.hasText(objeto.getSenha()) && objeto.getSenha().length() <= 30) {
			objeto.setSenha(encoder.encode(objeto.getSenha()));
		} else if (objeto.getId() == null) {
			objeto.setSenha(encoder.encode(objeto.getLogin()));
		} else {
			objeto.setSenha(repository.findSenhaById(objeto.getId()));
		}
	}

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public Usuario findByLogin() {
		try {
			return repository.findById(findByToken().getId()).get();
		} catch (Exception e) {
			logger.warn("findByToken " + ExceptionUtils.getRootCauseMessage(e));
		}
		return null;
	}

	@Override
	public ObjetoComIdDto findByToken() {
		try {
			final SecurityContext context = SecurityContextHolder.getContext();
			if (context != null) {
				final Authentication authentication = context.getAuthentication();
				if (authentication != null) {
					final Long id = repository.findIdByLogin(authentication.getName());
					return new ObjetoComIdDto(id);
				}
			}
		} catch (Exception e) {
			logger.warn("findByToken " + ExceptionUtils.getRootCauseMessage(e));
		}
		return null;
	}

	@Override
	public Optional<Usuario> findByLoginAndAtivoAndIgrejaAtivo(String login) {
		try {
			final AtivoInativoEnum ativo = AtivoInativoEnum.ATIVO;
			final Optional<Usuario> retorno = repository.findByLoginAndAtivoAndIgrejaAtivo(login, ativo, ativo);
			return retorno;
		} catch (Exception e) {
			logger.warn("findByLoginAndAtivoAndIgrejaAtivo " + ExceptionUtils.getRootCauseMessage(e));
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@Transactional
	@Override
	public void atualizarPermissoesPorCargo(Cargo cargo) {
		try {
			List<Usuario> usuarios = repository.findByPessoaCargoIdAndIdNot(cargo.getId(), 1l);
			if (!CollectionUtils.isEmpty(usuarios)) {
				usuarios.forEach(u -> u.setPermissoes(cargo.getPermissoes()));
				repository.saveAll(usuarios);
			}
		} catch (Exception e) {
			logger.warn("findByLoginAndAtivoAndIgrejaAtivo " + ExceptionUtils.getRootCauseMessage(e));
		}
	}

	@Override
	public Boolean existsByPessoaId(Long pessoaId) {
		try {
			return repository.existsByPessoaId(pessoaId);
		} catch (Exception e) {
			logger.warn("existsByPessoaId " + ExceptionUtils.getRootCauseMessage(e));
		}
		return Boolean.FALSE;
	}

	@Override
	public Usuario createNewByPessoa(Pessoa pessoa) {
		try {
			final String login = new Date().getTime() + "";
			Usuario usuario = new Usuario(login, login, pessoa.getIgreja(),
					permissaoService.findByCargosPessoasId(pessoa.getId()));
			return save(usuario);
		} catch (Exception e) {
			logger.warn("createNewByPessoa " + ExceptionUtils.getRootCauseMessage(e));
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@Override
	public Usuario tratar(Usuario objeto, OpcaoTratarEnum opcao) {
		if (objeto != null) {
			if (OpcaoTratarEnum.MOSTRAR.equals(opcao)) {
				objeto.setIgreja(objeto.getIgreja() != null ? new Igreja(objeto.getIgreja().getId()) : null);
			}
		}
		return objeto;
	}

	@Override
	public List<String> validar(UsuarioLsDto objeto, List<String> erros) throws Exception {
		if (objeto != null) {
			erros = ValidacaoComumUtil.validarString(objeto.getSenha(), "Senha", erros, 30);
			erros = ValidacaoComumUtil.validarString(objeto.getLogin(), "Login", 'o', erros, 100);
			Long id = 0l;
			if (objeto.getId() != null) {
				id = objeto.getId();
			}
			if (objeto.getLogin() != null && repository.existsByLoginAndIdNot(objeto.getLogin(), id)) {
				erros.add("Login em uso, por favor escolha outro.");
			}
		} else {
			erros.add("Dados do acesso devem ser informados.");
		}
		return erros;
	}

	@Transactional(readOnly = true)
	@Override
	public UsuarioLsDto tratar(Usuario usuario) {
		final UsuarioLsDto usuarioLsDto = new UsuarioLsDto(usuario.getId(), usuario.getLogin());
		return usuarioLsDto;
	}

	@Override
	public Usuario findByPessoaId(Long pessoaId) {
		try {
			return repository.findTopByPessoaId(pessoaId);
		} catch (Exception e) {
			logger.warn("findByPessoaId " + ExceptionUtils.getRootCauseMessage(e));
		}
		return null;
	}

	@Override
	public Usuario tratarUsuarioLsDtoPreSave(UsuarioLsDto dto, Long pessoaId, Long igrejaId, Long cargoId) {
		Usuario usuario = null;
		if (pessoaId != null) {
			Long aux = findIdTopByPessoaIdAndIdNot(pessoaId, dto.getId());
			if (aux != null) {
				dto.setId(aux);
			}
		}
		if (dto != null) {
			usuario = new Usuario(pessoaId == null ? null : dto.getId(), dto.getLogin(), dto.getSenha(),
					permissaoService.findByCargosId(cargoId));
			if (usuario != null && usuario.isUsuarioInformado()) {
				usuario.setIgreja(new Igreja(igrejaId));
				if (dto.getId() != null) {
					Usuario aux = findById(dto.getId());
					if (aux != null) {
						usuario.setDataCadastro(aux.getDataCadastro());
						usuario.setIdUsuarioCadastro(aux.getIdUsuarioCadastro());
						usuario.setAtivo(aux.getAtivo());
					}
				}
				usuario = save(usuario);
			} else {
				if (pessoaId != null) {
					usuario = findByPessoaId(pessoaId);
					if (usuario != null) {
						usuario.setSenha(null);
					}
				}
			}
		}
		return usuario;
	}

	@Override
	public UsuarioLogadoDto findUsuarioLogadoDtoBy() {
		try {
			final Long id = findByToken().getId();
			return repository.findUsuarioLogadoDtoById(id);
		} catch (Exception e) {
			logger.warn("findUsuarioLogadoDtoBy " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}

	@Override
	public Boolean reset(Long pessoaId) {
		try {
			Usuario usuario = findByPessoaId(pessoaId);
			usuario.setSenha(
					usuario.getLogin().length() > 29 ? usuario.getLogin().substring(0, 29) : usuario.getLogin());
			save(usuario);
			return Boolean.TRUE;
		} catch (Exception e) {
			logger.warn("reset " + ExceptionUtils.getRootCauseMessage(e));
			throw new NegocioException(e);
		}
	}
}
