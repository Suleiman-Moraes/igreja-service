package br.com.suleimanmoraes.igrejanewservice.api.service;

import java.util.List;

import br.com.suleimanmoraes.igrejanewservice.api.interfaces.CrudPadraoService;
import br.com.suleimanmoraes.igrejanewservice.api.model.Permissao;

public interface PermissaoService extends CrudPadraoService<Permissao>{

	List<Permissao> findByCargosPessoasId(Long pessoaId);

	List<Permissao> findByCargosId(Long cargoId);
}
