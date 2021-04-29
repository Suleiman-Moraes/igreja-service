package br.com.suleimanmoraes.igrejanewservice.api.dto.listagem;

import java.io.Serializable;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.MappedSuperclass;
import javax.persistence.SqlResultSetMapping;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Suleiman Moraes
 *
 */
@MappedSuperclass
@SqlResultSetMapping(name = PessoaListagemDto.PESSOA_LISTAGEM_DTO_MAPPING, classes = {
		@ConstructorResult(targetClass = PessoaListagemDto.class, columns = {
				@ColumnResult(name = "id", type = Long.class), 
				@ColumnResult(name = "nome", type = String.class),
				@ColumnResult(name = "login", type = String.class),
				@ColumnResult(name = "telefone", type = String.class),
				@ColumnResult(name = "cargoNome", type = String.class),
				@ColumnResult(name = "cargoId", type = Long.class),
				@ColumnResult(name = "igrejaId", type = Long.class),
				@ColumnResult(name = "ativo", type = String.class) }) })
@Data
@NoArgsConstructor
public class PessoaListagemDto implements Serializable {

	public static final String PESSOA_LISTAGEM_DTO_MAPPING = "PessoaListagemDtoMapping";

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String nome;
	
	private String login;
	
	private String telefone;
	
	private String cargoNome;
	
	private Long cargoId;

	private Long igrejaId;

	private AtivoInativoEnum ativo;

	public PessoaListagemDto(Long id, String nome, String login, String telefone, String cargoNome, Long cargoId,
			Long igrejaId, String ativo) {
		this.id = id;
		this.nome = nome;
		this.login = login;
		this.telefone = telefone;
		this.cargoNome = cargoNome;
		this.cargoId = cargoId;
		this.igrejaId = igrejaId;
		this.ativo = AtivoInativoEnum.getByValue(ativo);
	}

	public String getAtivoDescricao() {
		return ativo != null ? ativo.getDescricao() : null;
	}
}
