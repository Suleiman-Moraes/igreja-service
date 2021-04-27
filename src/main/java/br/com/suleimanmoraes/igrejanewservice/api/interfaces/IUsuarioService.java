package br.com.suleimanmoraes.igrejanewservice.api.interfaces;

import br.com.suleimanmoraes.igrejanewservice.api.dto.ObjetoComIdDto;

public interface IUsuarioService {

	ObjetoComIdDto findByToken();
}
