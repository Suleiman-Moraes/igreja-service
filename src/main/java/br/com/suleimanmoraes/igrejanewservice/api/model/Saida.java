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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "saida")
public class Saida implements Serializable, IDadosAlteracao {

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
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", locale = "pt-BR", timezone = "America/Sao_Paulo")
	@Column(name = "data_saida")
	private Date dataSaida;
	
	private Double valor;

	private String nome;

	private String descricao;

	@Convert(converter = AtivoInativoEnumConverter.class)
	private AtivoInativoEnum ativo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_saida_programada")
	private SaidaProgramada saidaProgramada;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_igreja")
	private Igreja igreja;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_forma_pagamento")
	private FormaPagamento formaPagamento;
	
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
		Saida other = (Saida) obj;
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
