package br.com.suleimanmoraes.igrejanewservice.api.service;

import java.util.List;

import br.com.suleimanmoraes.igrejanewservice.api.interfaces.CrudPadraoService;
import br.com.suleimanmoraes.igrejanewservice.api.model.Menu;

public interface MenuService extends CrudPadraoService<Menu>{

	List<Menu> findBy();
}
