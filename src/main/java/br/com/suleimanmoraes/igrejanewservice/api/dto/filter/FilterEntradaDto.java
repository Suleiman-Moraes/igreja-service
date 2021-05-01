package br.com.suleimanmoraes.igrejanewservice.api.dto.filter;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.interfaces.IFilterBasic;
import lombok.Data;

@Data
public class FilterEntradaDto implements IFilterBasic {
	private Integer page;
	
	private Integer size;
	
	private Long id;
	
	private Long igrejaId;
	
	private Long tipoEntradaId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", locale = "pt-BR", timezone = "America/Sao_Paulo")
	private Date mesAno;

	private String nomePessoa;
	
	private AtivoInativoEnum ativo;

	@Override
	public Integer getPage() {
		page = page == null ? 0 : page;
		return page;
	}

	@Override
	public Integer getSize() {
		size = size == null ? 10 : size;
		return size;
	}
}
