package br.com.suleimanmoraes.igrejanewservice.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.suleimanmoraes.igrejanewservice.api.converter.AtivoInativoEnumConverter;
import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.interfaces.IDadosAlteracao;
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
@Table(name = "usuario")
public class Usuario implements Serializable, IDadosAlteracao {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "data_hora_cadastro")
	private Date dataCadastro;

	@Column(name = "data_hora_alteracao")
	private Date dataAlteracao;

	@Column(name = "id_usuario_cadastro")
	private Long idUsuarioCadastro;

	@Column(name = "id_usuario_alteracao")
	private Long idUsuarioAlteracao;

	private String login;

	private String senha;

	@Convert(converter = AtivoInativoEnumConverter.class)
	private AtivoInativoEnum ativo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_igreja")
	private Igreja igreja;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_permissao", joinColumns = { @JoinColumn(name = "id_usuario") }, inverseJoinColumns = {
			@JoinColumn(name = "id_permissao") })
	private List<Permissao> permissoes;
	
	@JsonIgnoreProperties("usuario")
	@OneToOne(mappedBy = "usuario", fetch = FetchType.LAZY)
	private Pessoa pessoa;
	
	public Usuario(String login, String senha, Igreja igreja, List<Permissao> permissoes) {
		this.login = login;
		this.senha = senha;
		this.igreja = igreja;
		this.permissoes = permissoes;
	}
	public Usuario(Long id, String login, String senha, List<Permissao> permissoes) {
		this.senha = senha;
		this.login = login;
		this.id = id;
		this.permissoes = permissoes;
	}
	public Usuario(Long id) {
		this.id = id;
	}
	
	@JsonIgnore
	@Transient
	public Boolean isUsuarioInformado() {
		return id != null || login != null;
	}

	@PrePersist
	@Override
	public void prePersist() {
		ativo = ativo == null ? AtivoInativoEnum.ATIVO : ativo;
		prePersistAndUpdate();
	}

	@PreUpdate
	@Override
	public void preUpdate() {
		prePersistAndUpdate();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
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
