package br.com.suleimanmoraes.igrejanewservice.api.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.MappedSuperclass;
import javax.persistence.SqlResultSetMapping;

import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterEntradaDto;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Suleiman Moraes
 *
 */
@MappedSuperclass
@SqlResultSetMapping(name = EntradaInformacaoDto.ENTRADA_INFORMACAO_DTO_MAPPING, classes = {
		@ConstructorResult(targetClass = EntradaInformacaoDto.class, columns = {
				@ColumnResult(name = "totalRegistros", type = Integer.class),
				@ColumnResult(name = "valorTotal", type = Double.class),
				@ColumnResult(name = "media", type = Double.class) }) })
@Data
@NoArgsConstructor
public class EntradaInformacaoDto implements Serializable {

	public static final String ENTRADA_INFORMACAO_DTO_MAPPING = "EntradaInformacaoDtoMapping";

	private static final long serialVersionUID = 1L;

	private FilterEntradaDto filtro;
	
	private Integer totalRegistros = 0;
	
	private Double valorTotal = 0.0;

	private Double media = 0.0;
	
	private List<EntradaTipoEntradaInformacaoDto> tipoEntradaInfomacoes;

	public EntradaInformacaoDto(Integer totalRegistros, Double valorTotal, Double media) {
		this.totalRegistros = totalRegistros;
		this.valorTotal = valorTotal;
		this.media = media;
	}
}
