package br.com.suleimanmoraes.igrejanewservice.api.dto.listagem;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.MappedSuperclass;
import javax.persistence.SqlResultSetMapping;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Suleiman Moraes
 *
 */
@MappedSuperclass
@SqlResultSetMapping(name = SaidaListagemDto.SAIDA_LISTAGEM_DTO_MAPPING, classes = {
		@ConstructorResult(targetClass = SaidaListagemDto.class, columns = {
				@ColumnResult(name = "id", type = Long.class), 
				@ColumnResult(name = "dataSaida", type = Date.class),
				@ColumnResult(name = "valor", type = Double.class),
				@ColumnResult(name = "nome", type = String.class),
				@ColumnResult(name = "formaPagamentoNome", type = String.class),
				@ColumnResult(name = "igrejaId", type = Long.class),
				@ColumnResult(name = "ativo", type = String.class) }) })
@Data
@NoArgsConstructor
public class SaidaListagemDto implements Serializable {

	public static final String SAIDA_LISTAGEM_DTO_MAPPING = "SaidaListagemDtoMapping";

	private static final long serialVersionUID = 1L;

	private Long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", locale = "pt-BR", timezone = "America/Sao_Paulo")
	private Date dataSaida;
	
	private Double valor;
	
	private String nome;
	
	private String formaPagamentoNome;

	private Long igrejaId;

	private AtivoInativoEnum ativo;

	public SaidaListagemDto(Long id, Date dataSaida, Double valor, String nome, String formaPagamentoNome,
			Long igrejaId, String ativo) {
		this.id = id;
		this.dataSaida = dataSaida;
		this.valor = valor;
		this.nome = nome;
		this.formaPagamentoNome = formaPagamentoNome;
		this.igrejaId = igrejaId;
		this.ativo = AtivoInativoEnum.getByValue(ativo);
	}

	public String getAtivoDescricao() {
		return ativo != null ? ativo.getDescricao() : null;
	}
}
