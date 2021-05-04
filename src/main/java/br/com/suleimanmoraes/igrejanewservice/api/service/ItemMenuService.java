package br.com.suleimanmoraes.igrejanewservice.api.service;

import java.util.List;

import br.com.suleimanmoraes.igrejanewservice.api.interfaces.CrudPadraoService;
import br.com.suleimanmoraes.igrejanewservice.api.model.ItemMenu;

public interface ItemMenuService extends CrudPadraoService<ItemMenu>{

	Boolean ativar(Long id) throws Exception;

	List<ItemMenu> findByAtivoAndPermissoesUsuariosIdAndMenuId(Long usuarioId, Long menuId);
}
