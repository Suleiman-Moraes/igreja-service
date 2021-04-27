package br.com.suleimanmoraes.igrejanewservice.api.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.suleimanmoraes.igrejanewservice.api.model.Estado;
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
public class PessoaDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String nome;

	private String telefone;

	private String email;

	private String cpf;

	private String cidade;

	private Date nascimento;

	private EnderecoDto endereco;
	
	private Estado estado;

	private ObjetoComIdDto igreja;

	private ObjetoComIdDto cargo;

	private UsuarioLsDto usuario;

	public PessoaDto(Long id) {
		this.id = id;
	}
}
