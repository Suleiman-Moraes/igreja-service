package br.com.suleimanmoraes.igrejanewservice.api.dto.listagem;

import java.io.Serializable;
import java.util.Date;

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
@SqlResultSetMapping(name = EntradaListagemDto.ENTRADA_LISTAGEM_DTO_MAPPING, classes = {
		@ConstructorResult(targetClass = EntradaListagemDto.class, columns = {
				@ColumnResult(name = "id", type = Long.class), 
				@ColumnResult(name = "dataEntrada", type = Date.class),
				@ColumnResult(name = "valor", type = Double.class),
				@ColumnResult(name = "nome", type = String.class),
				@ColumnResult(name = "formaPagamentoNome", type = String.class),
				@ColumnResult(name = "igrejaId", type = Long.class),
				@ColumnResult(name = "tipoEntradaNome", type = String.class),
				@ColumnResult(name = "tipoEntradaId", type = Long.class),
				@ColumnResult(name = "pessoaNome", type = String.class),
				@ColumnResult(name = "pessoaId", type = Long.class),
				@ColumnResult(name = "ativo", type = String.class) }) })
@Data
@NoArgsConstructor
public class EntradaListagemDto implements Serializable {

	public static final String ENTRADA_LISTAGEM_DTO_MAPPING = "EntradaListagemDtoMapping";

	private static final long serialVersionUID = 1L;

	private Long id;

	private Date dataEntrada;
	
	private Double valor;
	
	private String nome;
	
	private String formaPagamentoNome;
	
	private String tipoEntradaNome;
	
	private Long tipoEntradaId;
	
	private String pessoaNome;
	
	private Long pessoaId;

	private Long igrejaId;

	private AtivoInativoEnum ativo;
	
	public EntradaListagemDto(Long id, Date dataEntrada, Double valor, String nome, String formaPagamentoNome,
			String tipoEntradaNome, Long tipoEntradaId, String pessoaNome, Long pessoaId, Long igrejaId,
			String ativo) {
		this.id = id;
		this.dataEntrada = dataEntrada;
		this.valor = valor;
		this.nome = nome;
		this.formaPagamentoNome = formaPagamentoNome;
		this.tipoEntradaNome = tipoEntradaNome;
		this.tipoEntradaId = tipoEntradaId;
		this.pessoaNome = pessoaNome;
		this.pessoaId = pessoaId;
		this.igrejaId = igrejaId;
		this.ativo = AtivoInativoEnum.getByValue(ativo);
	}

	public String getAtivoDescricao() {
		return ativo != null ? ativo.getDescricao() : null;
	}
}
