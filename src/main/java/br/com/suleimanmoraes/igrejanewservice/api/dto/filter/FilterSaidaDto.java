package br.com.suleimanmoraes.igrejanewservice.api.dto.filter;

import java.util.Date;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.interfaces.IFilterBasic;
import lombok.Data;

@Data
public class FilterSaidaDto implements IFilterBasic {
	private Integer page;
	
	private Integer size;
	
	private Long id;
	
	private Long igrejaId;

	private Date mesAno;
	
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
