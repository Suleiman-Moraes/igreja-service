package br.com.suleimanmoraes.igrejanewservice.api.service;

import br.com.suleimanmoraes.igrejanewservice.api.interfaces.CrudPadraoService;
import br.com.suleimanmoraes.igrejanewservice.api.model.Igreja;

public interface IgrejaService extends CrudPadraoService<Igreja>{

	Igreja findByToken();
}
