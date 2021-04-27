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
public class UsuarioLogadoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String login;

	private String nome;
	
	private String igreja;

	private Long igrejaId;

	public UsuarioLogadoDto(Long id, String login, String nome, String igreja, Long igrejaId) {
		this.id = id;
		this.login = login;
		this.nome = nome;
		this.igreja = igreja;
		this.igrejaId = igrejaId;
	}
	
	public String getNome() {
		return nome != null ? nome : login;
	}
}
