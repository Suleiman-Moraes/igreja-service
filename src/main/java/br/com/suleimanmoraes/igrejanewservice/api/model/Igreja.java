package br.com.suleimanmoraes.igrejanewservice.api.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.suleimanmoraes.igrejanewservice.api.converter.AtivoInativoEnumConverter;
import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
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
@Entity
@Table(name = "igreja")
public class Igreja implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;

	@Convert(converter = AtivoInativoEnumConverter.class)
	private AtivoInativoEnum ativo;
	
	@JsonIgnore
	@OneToMany(mappedBy = "igreja", fetch = FetchType.LAZY)
	private List<Usuario> usuarios;
	
	@JsonIgnore
	@OneToMany(mappedBy = "igreja", fetch = FetchType.LAZY)
	private List<Pessoa> pessoas;
	
	@JsonIgnore
	@OneToMany(mappedBy = "igreja", fetch = FetchType.LAZY)
	private List<SaidaProgramada> saidaProgramadas;
	
	@JsonIgnore
	@OneToMany(mappedBy = "igreja", fetch = FetchType.LAZY)
	private List<Saida> saidas;
	
	public Igreja(Long id) {
		this.id = id;
	}
	
	@PrePersist
	public void prePersist() {
		ativo = ativo == null ? AtivoInativoEnum.ATIVO : ativo;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Igreja other = (Igreja) obj;
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
