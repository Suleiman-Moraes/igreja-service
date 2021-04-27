package br.com.suleimanmoraes.igrejanewservice.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.suleimanmoraes.igrejanewservice.api.converter.AtivoInativoEnumConverter;
import br.com.suleimanmoraes.igrejanewservice.api.dto.PessoaDto;
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
@Table(name = "pessoa")
public class Pessoa implements Serializable, IDadosAlteracao {

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

	private String nome;

	private String telefone;

	private String email;

	private String cpf;

	private String cidade;

	private Date nascimento;

	@Convert(converter = AtivoInativoEnumConverter.class)
	private AtivoInativoEnum ativo;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_estado")
	private Estado estado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_igreja")
	private Igreja igreja;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cargo")
	private Cargo cargo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_endereco")
	private Endereco endereco;

	@JsonIgnoreProperties(value = { "pessoa", "permissoes" })
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_usuario", referencedColumnName = "id")
	private Usuario usuario;

	public Pessoa(long id) {
		this.id = id;
	}

	public Pessoa(String nome, Igreja igreja, Cargo cargo) {
		this.nome = nome;
		this.igreja = igreja;
		this.cargo = cargo;
	}

	public void setPessoa(PessoaDto objeto) {
		this.nome = objeto.getNome();
		this.telefone = objeto.getTelefone();
		this.email = objeto.getEmail();
		this.cpf = objeto.getCpf();
		this.cidade = objeto.getCidade();
		this.nascimento = objeto.getNascimento();
		this.estado = objeto.getEstado();
		this.igreja = objeto.getIgreja() != null ? new Igreja(objeto.getIgreja().getId()) : null;
		this.cargo = objeto.getCargo() != null ? new Cargo(objeto.getCargo().getId()) : null;
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
		Pessoa other = (Pessoa) obj;
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
