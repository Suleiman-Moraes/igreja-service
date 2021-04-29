package br.com.suleimanmoraes.igrejanewservice.api.dto;

import java.io.Serializable;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.MappedSuperclass;
import javax.persistence.SqlResultSetMapping;

import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterSaidaDto;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Suleiman Moraes
 *
 */
@MappedSuperclass
@SqlResultSetMapping(name = SaidaInformacaoDto.SAIDA_INFORMACAO_DTO_MAPPING, classes = {
		@ConstructorResult(targetClass = SaidaInformacaoDto.class, columns = {
				@ColumnResult(name = "totalRegistros", type = Integer.class),
				@ColumnResult(name = "valorTotal", type = Double.class),
				@ColumnResult(name = "media", type = Double.class) }) })
@Data
@NoArgsConstructor
public class SaidaInformacaoDto implements Serializable {

	public static final String SAIDA_INFORMACAO_DTO_MAPPING = "SaidaInformacaoDtoMapping";

	private static final long serialVersionUID = 1L;

	private FilterSaidaDto filtro;
	
	private Integer totalRegistros = 0;
	
	private Double valorTotal = 0.0;

	private Double media = 0.0;

	public SaidaInformacaoDto(Integer totalRegistros, Double valorTotal, Double media) {
		this.totalRegistros = totalRegistros;
		this.valorTotal = valorTotal;
		this.media = media;
	}
}
