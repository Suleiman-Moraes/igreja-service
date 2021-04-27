package br.com.suleimanmoraes.igrejanewservice.api.service;

import java.util.List;
import java.util.Optional;

import br.com.suleimanmoraes.igrejanewservice.api.dto.UsuarioLogadoDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.UsuarioLsDto;
import br.com.suleimanmoraes.igrejanewservice.api.interfaces.CrudPadraoService;
import br.com.suleimanmoraes.igrejanewservice.api.interfaces.IUsuarioService;
import br.com.suleimanmoraes.igrejanewservice.api.model.Cargo;
import br.com.suleimanmoraes.igrejanewservice.api.model.Pessoa;
import br.com.suleimanmoraes.igrejanewservice.api.model.Usuario;

public interface UsuarioService extends IUsuarioService, CrudPadraoService<Usuario>{

	Optional<Usuario> findByLoginAndAtivoAndIgrejaAtivo(String login);

	void atualizarPermissoesPorCargo(Cargo cargo);
	
	Boolean existsByPessoaId(Long pessoaId);

	Usuario createNewByPessoa(Pessoa pessoa);

	Usuario findByLogin();

	List<String> validar(UsuarioLsDto objeto, List<String> erros) throws Exception;

	UsuarioLsDto tratar(Usuario usuario);

	Usuario findByPessoaId(Long pessoaId);

	Usuario tratarUsuarioLsDtoPreSave(UsuarioLsDto usuarioLsDto, Long pessoaId, Long igrejaId, Long cargoId);

	UsuarioLogadoDto findUsuarioLogadoDtoBy();
}
