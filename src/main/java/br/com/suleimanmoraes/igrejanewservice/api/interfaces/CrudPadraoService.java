package br.com.suleimanmoraes.igrejanewservice.api.interfaces;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import br.com.suleimanmoraes.igrejanewservice.api.dto.ObjetoComIdDto;
import br.com.suleimanmoraes.igrejanewservice.api.enums.OpcaoTratarEnum;
import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;

public interface CrudPadraoService<T> extends ICrudPadraoService<T> {

	JpaRepository<T, Long> getRepository();

	Log getLogger();

	@Override
	@Transactional(readOnly = true)
	default List<T> findAll() {
		try {
			List<T> objetos = getRepository().findAll();
			getLogger().info("findAll " + objetos.size());
			return objetos;
		} catch (Exception e) {
			getLogger().warn("findAll " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	default T findById(Long id) {
		try {
			T objeto = getRepository().findById(id).get();
			getLogger().info("findById " + id);
			return objeto;
		} catch (Exception e) {
			getLogger().warn("findById " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}

	@Override
	@Transactional
	default T save(T objeto) throws NegocioException {
		try {
			preSave(objeto);
			objeto = getRepository().save(objeto);
			posSave(objeto);
			return objeto;
		} catch (DataIntegrityViolationException e) {
			getLogger().warn("save " + ExceptionUtils.getRootCauseMessage(e));
			throw new NegocioException("Dados inseridos de forma incorreta.", e);
		} catch (Exception e) {
			getLogger().warn("save " + ExceptionUtils.getRootCauseMessage(e));
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@Override
	default Boolean deleteById(Long id) throws Exception {
		getLogger().warn("Não pode ser excluído");
		return Boolean.FALSE;
	}

	@Override
	@Transactional(readOnly = true)
	default void validar(T objeto) throws Exception {
	}

	@Override
	@Transactional(readOnly = true)
	default Page<T> paginarComParemetros(Integer page, Integer size) {
		try {
			Page<T> pagina = getRepository().findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
			getLogger().info(String.format("paginarComParemetros(%s, %s) == pagina.getContent().size() = ", page, size,
					pagina.getContent().size()));
			return pagina;
		} catch (Exception e) {
			getLogger().warn("paginarComParemetros" + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	default T tratar(T objeto, OpcaoTratarEnum opcao) {
		return objeto;
	};

	default void preSave(T objeto) throws Exception {
	}

	default void posSave(T objeto) throws Exception {
	}

	default T saveUsuarioAlteracaoAndCadastro(T objeto, IUsuarioService usuarioService) {
		if (objeto != null && objeto instanceof IDadosAlteracao) {
			ObjetoComIdDto usuarioDto = usuarioService.findByToken();
			if (usuarioDto != null) {
				((IDadosAlteracao) objeto).setIdUsuarioAlteracao(usuarioDto.getId());
				((IDadosAlteracao) objeto).setIdUsuarioCadastro(
						((IDadosAlteracao) objeto).getIdUsuarioCadastro() == null ? usuarioDto.getId()
								: ((IDadosAlteracao) objeto).getIdUsuarioCadastro());
			}
		}
		return objeto;
	}
}
