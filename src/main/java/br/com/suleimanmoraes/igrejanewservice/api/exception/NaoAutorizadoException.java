package br.com.suleimanmoraes.igrejanewservice.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NaoAutorizadoException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
}
