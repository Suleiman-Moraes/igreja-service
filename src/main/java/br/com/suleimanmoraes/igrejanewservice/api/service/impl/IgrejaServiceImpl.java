package br.com.suleimanmoraes.igrejanewservice.api.service.impl;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.suleimanmoraes.igrejanewservice.api.dto.ObjetoComIdDto;
import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.model.Igreja;
import br.com.suleimanmoraes.igrejanewservice.api.repository.IgrejaRepository;
import br.com.suleimanmoraes.igrejanewservice.api.service.IgrejaService;
import br.com.suleimanmoraes.igrejanewservice.api.service.UsuarioService;
import lombok.Getter;

@Service
public class IgrejaServiceImpl implements IgrejaService {

	private static final Log logger = LogFactory.getLog(IgrejaService.class);

	@Getter
	@Autowired
	private IgrejaRepository repository;
	
	@Autowired
	private UsuarioService usuarioService;

	@Override
	public Log getLogger() {
		return logger;
	}
	
	@Override
	public Igreja findByToken() {
		try {
			ObjetoComIdDto dto = usuarioService.findByToken();
			return repository.findTopByUsuariosId(dto.getId());
		} catch (Exception e) {
			logger.warn("findByToken " + ExceptionUtils.getRootCauseMessage(e));
		}
		return null;
	}
	
	@Override
	public List<Igreja> findAll() {
		try {
			return repository.findByAtivoOrderByNomeAsc(AtivoInativoEnum.ATIVO);
		} catch (Exception e) {
			logger.warn("findAll " + ExceptionUtils.getRootCauseMessage(e));
			return null;
		}
	}
}
