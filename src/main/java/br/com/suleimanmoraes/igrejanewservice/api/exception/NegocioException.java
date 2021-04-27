package br.com.suleimanmoraes.igrejanewservice.api.exception;

import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NegocioException extends RuntimeException{
	
	@Getter
	List<String> erros = new LinkedList<>();

	private static final long serialVersionUID = 1L;
	
	public NegocioException(String mensagem) {
		super(mensagem);
	}

	public NegocioException(String mensagem, Throwable e) {
		super(e);
		erros.add(mensagem);
	}

	public NegocioException(Throwable e) {
		super(e);
	}
	
	public NegocioException(List<String> erros) {
		this.erros = erros;
	}
}
