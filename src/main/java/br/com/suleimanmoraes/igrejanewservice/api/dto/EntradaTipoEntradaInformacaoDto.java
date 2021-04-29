package br.com.suleimanmoraes.igrejanewservice.api.dto;

import java.io.Serializable;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.MappedSuperclass;
import javax.persistence.SqlResultSetMapping;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Suleiman Moraes
 *
 */
@MappedSuperclass
@SqlResultSetMapping(name = EntradaTipoEntradaInformacaoDto.ENTRADA_TIPO_ENTRADA_INFORMACAO_DTO_MAPPING, classes = {
		@ConstructorResult(targetClass = EntradaTipoEntradaInformacaoDto.class, columns = {
				@ColumnResult(name = "totalRegistros", type = Integer.class),
				@ColumnResult(name = "valorTotal", type = Double.class),
				@ColumnResult(name = "media", type = Double.class),
				@ColumnResult(name = "tipoEntradaId", type = Long.class),
				@ColumnResult(name = "tipoEntradaNome", type = String.class) }) })
@Data
@NoArgsConstructor
public class EntradaTipoEntradaInformacaoDto implements Serializable {

	public static final String ENTRADA_TIPO_ENTRADA_INFORMACAO_DTO_MAPPING = "EntradaTipoEntradaInformacaoDtoMapping";

	private static final long serialVersionUID = 1L;

	private Integer totalRegistros = 0;
	
	private Double valorTotal = 0.0;
	
	private Double media = 0.0;

	private Long tipoEntradaId;

	private String tipoEntradaNome;

	public EntradaTipoEntradaInformacaoDto(Integer totalRegistros, Double valorTotal, Double media, Long tipoEntradaId,
			String tipoEntradaNome) {
		this.totalRegistros = totalRegistros;
		this.valorTotal = valorTotal;
		this.media = media;
		this.tipoEntradaId = tipoEntradaId;
		this.tipoEntradaNome = tipoEntradaNome;
	}
}
