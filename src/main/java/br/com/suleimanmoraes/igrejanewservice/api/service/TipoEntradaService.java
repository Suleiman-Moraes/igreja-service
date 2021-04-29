package br.com.suleimanmoraes.igrejanewservice.api.service;

import br.com.suleimanmoraes.igrejanewservice.api.interfaces.CrudPadraoService;
import br.com.suleimanmoraes.igrejanewservice.api.model.TipoEntrada;

public interface TipoEntradaService extends CrudPadraoService<TipoEntrada> {

	Boolean ativar(Long id) throws Exception;
}
