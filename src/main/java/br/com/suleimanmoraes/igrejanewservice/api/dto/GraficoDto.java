package br.com.suleimanmoraes.igrejanewservice.api.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Suleiman Moraes
 *
 */
@Data
@NoArgsConstructor
public class GraficoDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String label;
	
	private String backgroundColor;
	
	private String borderColor = "#fff";
	
	private Integer[] data;

	public GraficoDto(String label, String backgroundColor) {
		this.label = label;
		this.backgroundColor = backgroundColor;
	}
}
