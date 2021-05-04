package br.com.suleimanmoraes.igrejanewservice.api.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;
import br.com.suleimanmoraes.igrejanewservice.api.model.Cargo;
import br.com.suleimanmoraes.igrejanewservice.api.repository.CargoRepository;
import br.com.suleimanmoraes.igrejanewservice.api.service.CargoService;
import br.com.suleimanmoraes.igrejanewservice.api.service.UsuarioService;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@Service
public class CargoServiceImpl implements CargoService {

	private static final Log logger = LogFactory.getLog(CargoService.class);

	@Getter
	@Autowired
	private CargoRepository repository;

	@Autowired
	private UsuarioService usuarioService;

	@Override
	public void preSave(Cargo objeto) {
		saveUsuarioAlteracaoAndCadastro(objeto, usuarioService);
	}

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public void validar(Cargo objeto) throws NegocioException {
		List<String> erros = new LinkedList<>();
		erros = ValidacaoComumUtil.validarString(objeto.getNome(), "Nome", 'o', erros, 100);
		erros = ValidacaoComumUtil.validarString(objeto.getDescricao(), "Descrição", erros, 300);
		if (!CollectionUtils.isEmpty(erros)) {
			throw new NegocioException(erros);
		}
	}

	@Override
	public void posSave(Cargo objeto) throws Exception {
		new Thread(() -> {
			try {
				usuarioService.atualizarPermissoesPorCargo(objeto);
			} catch (Exception e) {
				logger.warn("posSave " + ExceptionUtils.getRootCauseMessage(e));
			}
		}).start();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cargo> paginarComParemetros(Integer page, Integer size) {
		try {
			Page<Cargo> pagina = repository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));
			return pagina;
		} catch (Exception e) {
			logger.warn("paginarComParemetros " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}
	
	@Override
	public List<Cargo> findAll() {
		try {
			List<Cargo> retorno = repository.findByAtivo(AtivoInativoEnum.ATIVO);
			return retorno;
		} catch (Exception e) {
			logger.warn("findAll " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}
	
	@Override
	public Boolean ativar(Long id) throws Exception {
		try {
			Cargo objeto = findById(id);
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
			Cargo objeto = findById(id);
			objeto.setAtivo(AtivoInativoEnum.INATIVO);
			save(objeto);
			return Boolean.TRUE;
		} catch (Exception e) {
			logger.warn("deleteById " + ExceptionUtils.getRootCauseMessage(e));
			throw e;
		}
	}
}
