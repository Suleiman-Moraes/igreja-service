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
import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;
import br.com.suleimanmoraes.igrejanewservice.api.model.ItemMenu;
import br.com.suleimanmoraes.igrejanewservice.api.repository.ItemMenuRepository;
import br.com.suleimanmoraes.igrejanewservice.api.service.ItemMenuService;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@Service
public class ItemMenuServiceImpl implements ItemMenuService {

	private static final Log logger = LogFactory.getLog(ItemMenuService.class);

	@Getter
	@Autowired
	private ItemMenuRepository repository;

	@Override
	public Log getLogger() {
		return logger;
	}

	@Override
	public void validar(ItemMenu objeto) throws NegocioException {
		List<String> erros = new LinkedList<>();
		erros = ValidacaoComumUtil.validarString(objeto.getNome(), "Nome", 'o', erros, 50);
		erros = ValidacaoComumUtil.validarString(objeto.getIcon(), "Ícone", 'o', erros, 50);
		erros = ValidacaoComumUtil.validarString(objeto.getUrl(), "Url", 'a', erros, 100);
		if (!CollectionUtils.isEmpty(erros)) {
			throw new NegocioException(erros);
		}
	}

	@Override
	public Boolean ativar(Long id) throws Exception {
		try {
			ItemMenu objeto = findById(id);
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
			ItemMenu objeto = findById(id);
			objeto.setAtivo(AtivoInativoEnum.INATIVO);
			save(objeto);
			return Boolean.TRUE;
		} catch (Exception e) {
			logger.warn("deleteById " + ExceptionUtils.getRootCauseMessage(e));
			throw e;
		}
	}

	@Override
	public List<ItemMenu> findByAtivoAndPermissoesUsuariosIdAndMenuId(Long usuarioId, Long menuId) {
		try {
			final Set<ItemMenu> lista = repository
					.findByAtivoAndPermissoesUsuariosIdAndMenuId(AtivoInativoEnum.ATIVO, usuarioId, menuId);
			return new LinkedList<>(lista);
		} catch (Exception e) {
			logger.warn("findByAtivoAndPermissoesUsuariosIdAndMenuId " + ExceptionUtils.getRootCauseMessage(e));
		}
		return null;
	}
}
