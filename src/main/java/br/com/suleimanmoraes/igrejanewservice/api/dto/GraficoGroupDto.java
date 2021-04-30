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
@SqlResultSetMapping(name = GraficoGroupDto.GRAFICO_GROUP_DTO_MAPPING, classes = {
		@ConstructorResult(targetClass = GraficoGroupDto.class, columns = {
				@ColumnResult(name = "valor", type = Double.class), 
				@ColumnResult(name = "mes", type = Integer.class) }) })
@Data
@NoArgsConstructor
public class GraficoGroupDto implements Serializable {
	
	public static final String GRAFICO_GROUP_DTO_MAPPING = "GraficoGroupDtoMapping";

	private static final long serialVersionUID = 1L;
	
	private Double valor;
	
	private Integer mes;

	public GraficoGroupDto(Double valor, Integer mes) {
		this.valor = valor;
		this.mes = mes;
	}
}
