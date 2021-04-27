package br.com.suleimanmoraes.igrejanewservice.api.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Suleiman Moraes
 *
 */
@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
public class UsuarioLsDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String login;

	private String senha;

	public UsuarioLsDto(Long id) {
		this.id = id;
	}
	public UsuarioLsDto(Long id, String login) {
		this.id = id;
		this.login = login;
	}
}
