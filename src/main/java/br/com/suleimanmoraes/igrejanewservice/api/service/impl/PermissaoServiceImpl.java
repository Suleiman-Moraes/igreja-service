package br.com.suleimanmoraes.igrejanewservice.api.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;
import br.com.suleimanmoraes.igrejanewservice.api.model.Permissao;
import br.com.suleimanmoraes.igrejanewservice.api.model.Usuario;
import br.com.suleimanmoraes.igrejanewservice.api.repository.PermissaoRepository;
import br.com.suleimanmoraes.igrejanewservice.api.service.PermissaoService;
import br.com.suleimanmoraes.igrejanewservice.api.service.UsuarioService;
import br.com.suleimanmoraes.igrejanewservice.api.util.ValidacaoComumUtil;
import lombok.Getter;

@Service
public class PermissaoServiceImpl implements PermissaoService {

	private static final Log logger = LogFactory.getLog(PermissaoService.class);

	@Getter
	@Autowired
	private PermissaoRepository repository;
	
	@Autowired
	private UsuarioService usuarioService;

	@Override
	public Log getLogger() {
		return logger;
	}
	
	@Override
	public void posSave(Permissao objeto) throws Exception {
		new Thread(() -> {
			try {
				if(!repository.existsByIdAndUsuariosId(objeto.getId(), 1l)) {
					Usuario usuario = usuarioService.findById(1l);
					usuario.getPermissoes().add(objeto);
					usuarioService.save(usuario);
				}
			} catch (Exception e) {
				logger.warn("posSave " + ExceptionUtils.getRootCauseMessage(e));
			}
		}).start();
	}
	
	@Override
	public void validar(Permissao objeto) throws NegocioException{
		List<String> erros = new LinkedList<>();
		erros = ValidacaoComumUtil.validarString(objeto.getNome(), "Nome", 'o', erros, 100);
		erros = ValidacaoComumUtil.validarString(objeto.getDescricao(), "Descrição", erros, 300);
		if(!CollectionUtils.isEmpty(erros)) {
			throw new NegocioException(erros);
		}
	}
	
	@Override
	public List<Permissao> findByCargosPessoasId(Long pessoaId){
		try {
			return repository.findByCargosPessoasId(pessoaId);
		} catch (Exception e) {
			logger.warn("findByCargosPessoasId " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}
	
	@Override
	public List<Permissao> findByCargosId(Long cargoId){
		try {
			return repository.findByCargosId(cargoId);
		} catch (Exception e) {
			logger.warn("findByCargosId " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}
}
