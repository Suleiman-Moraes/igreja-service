package br.com.suleimanmoraes.igrejanewservice.api.enums;

import org.springframework.util.StringUtils;

import lombok.Getter;

/**
 * 
 * @author Suleiman Moraes
 *
 */
@Getter
public enum AtivoInativoEnum {
	ATIVO("Ativo", "Ativo"), 
	INATIVO("Inativo", "Inativo");
	
	private String value;
	private String descricao;

	private AtivoInativoEnum(String value, String descricao) {
		this.value = value;
		this.descricao = descricao;
	};

	public static AtivoInativoEnum getByValue(String value) {
		if (StringUtils.hasText(value)) {
			for (AtivoInativoEnum tipo : AtivoInativoEnum.values()) {
				if (tipo.getValue().equals(value)) {
					return tipo;
				}
			}
		}
		return null;
	}
}
