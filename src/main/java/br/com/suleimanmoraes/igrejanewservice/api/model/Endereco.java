package br.com.suleimanmoraes.igrejanewservice.api.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.suleimanmoraes.igrejanewservice.api.dto.EnderecoDto;
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
@Table(name = "endereco")
public class Endereco implements Serializable, IDadosAlteracao {

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

	private String bairro;
	
	private String numero;

	private String quadra;

	private String lote;

	private String complemento;

	private String cep;

	private String rua;

	private String cidade;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_estado")
	private Estado estado;

	@JsonIgnore
	@OneToMany(mappedBy = "endereco", fetch = FetchType.LAZY)
	private List<Pessoa> pessoas;

	public Endereco(Long id) {
		this.id = id;
	}
	public Endereco(EnderecoDto objeto) {
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
	public Endereco(Endereco objeto) {
		this.id = objeto.getId();
		this.dataCadastro = objeto.getDataCadastro();
		this.dataAlteracao = objeto.getDataAlteracao();
		this.idUsuarioCadastro = objeto.getIdUsuarioCadastro();
		this.idUsuarioAlteracao = objeto.getIdUsuarioAlteracao();
		this.numero = objeto.getNumero();
		this.quadra = objeto.getQuadra();
		this.lote = objeto.getLote();
		this.complemento = objeto.getComplemento();
		this.cep = objeto.getCep();
		this.rua = objeto.getRua();
		this.bairro = objeto.getBairro();
		this.cidade = objeto.getCidade();
		this.estado = objeto.getEstado();
		this.pessoas = objeto.getPessoas();
	}

	@JsonIgnore
	@Transient
	public Boolean isEnderecoInformado() {
		return id != null || numero != null || quadra != null || lote != null || complemento != null || cep != null
				|| rua != null || cidade != null || (estado != null && estado.getId() != null);
	}

	@PrePersist
	@Override
	public void prePersist() {
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
		Endereco other = (Endereco) obj;
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
