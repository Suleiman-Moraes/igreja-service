package br.com.suleimanmoraes.igrejanewservice.api.model;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "item_menu")
public class ItemMenu implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;

	private String icon;

	private String url;

	@Convert(converter = AtivoInativoEnumConverter.class)
	private AtivoInativoEnum ativo;

	@JsonIgnoreProperties({ "itemMenus" })
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_menu")
	private Menu menu;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "item_menu_permissao", joinColumns = {
			@JoinColumn(name = "id_item_menu") }, inverseJoinColumns = { @JoinColumn(name = "id_permissao") })
	private List<Permissao> permissoes;

	public ItemMenu(Long id) {
		this.id = id;
	}
	public ItemMenu(Long id, String nome, String icon, String url) {
		this.id = id;
		this.nome = nome;
		this.icon = icon;
		this.url = url;
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
		ItemMenu other = (ItemMenu) obj;
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
