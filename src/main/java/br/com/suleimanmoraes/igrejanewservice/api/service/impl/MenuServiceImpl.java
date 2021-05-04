package br.com.suleimanmoraes.igrejanewservice.api.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.enums.OpcaoTratarEnum;
import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;
import br.com.suleimanmoraes.igrejanewservice.api.model.Menu;
import br.com.suleimanmoraes.igrejanewservice.api.repository.MenuRepository;
import br.com.suleimanmoraes.igrejanewservice.api.service.ItemMenuService;
import br.com.suleimanmoraes.igrejanewservice.api.service.MenuService;
import br.com.suleimanmoraes.igrejanewservice.api.service.UsuarioService;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@Service
public class MenuServiceImpl implements MenuService {

	private static final Log logger = LogFactory.getLog(MenuService.class);

	@Getter
	@Autowired
	private MenuRepository repository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ItemMenuService itemMenuService;

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public void validar(Menu objeto) throws NegocioException {
		List<String> erros = new LinkedList<>();
		erros = ValidacaoComumUtil.validarString(objeto.getNome(), "Nome", 'o', erros, 50);
		if (!CollectionUtils.isEmpty(erros)) {
			throw new NegocioException(erros);
		}
	}

	@Override
	public List<Menu> findAll() {
		try {
			final Long usuarioId = usuarioService.findByToken().getId();
			Set<Menu> retorno = repository
					.findByItemMenusAtivoAndItemMenusPermissoesUsuariosId(AtivoInativoEnum.ATIVO.getValue(), usuarioId);
			retorno.forEach(menu -> menu.setItemMenus(
					itemMenuService.findByAtivoAndPermissoesUsuariosIdAndMenuId(usuarioId, menu.getId())));
			return new LinkedList<>(retorno);
		} catch (Exception e) {
			logger.warn("findAll " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}

	@Override
	public Menu tratar(Menu objeto, OpcaoTratarEnum opcao) {
		if (objeto != null) {
			if (OpcaoTratarEnum.MOSTRAR.equals(opcao)) {
				objeto.setItemMenus(null);
			}
		}
		return objeto;
	}
	
	@Override
	public List<Menu> findBy() {
		try {
			return repository.findBy();
		} catch (Exception e) {
			logger.warn("findBy " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}
}
