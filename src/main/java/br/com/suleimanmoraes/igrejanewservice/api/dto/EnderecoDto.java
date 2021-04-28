package br.com.suleimanmoraes.igrejanewservice.api.dto;

import java.io.Serializable;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.suleimanmoraes.igrejanewservice.api.model.Endereco;
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
public class EnderecoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String bairro;

	private String numero;

	private String quadra;

	private String lote;

	private String complemento;

	private String cep;

	private String rua;

	private String cidade;

	private Estado estado;

	public EnderecoDto(Long id) {
		this.id = id;
	}
	public EnderecoDto(Endereco objeto) {
		this.id = objeto.getId();
		this.numero = objeto.getNumero();
		this.quadra = objeto.getQuadra();
		this.lote = objeto.getLote();
		this.complemento = objeto.getComplemento();
		this.cep = objeto.getCep();
		this.rua = objeto.getRua();
		this.bairro = objeto.getBairro();
		this.cidade = objeto.getCidade();
		this.estado = objeto.getEstado();
	}

	@JsonIgnore
	@Transient
	public Boolean isEnderecoInformado() {
		return id != null || numero != null || quadra != null || lote != null || complemento != null || cep != null
				|| rua != null || cidade != null || (estado != null && estado.getId() != null);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnderecoDto other = (EnderecoDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
}
