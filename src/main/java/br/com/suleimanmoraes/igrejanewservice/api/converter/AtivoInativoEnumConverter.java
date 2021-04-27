package br.com.suleimanmoraes.igrejanewservice.api.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;

/**
 * 
 * @author Suleiman Moraes
 *
 */
@Converter(autoApply = true)
public class AtivoInativoEnumConverter implements AttributeConverter<AtivoInativoEnum, String>{

	@Override
	public String convertToDatabaseColumn(AtivoInativoEnum attribute) {
		 return attribute.getValue();
	}

	@Override
	public AtivoInativoEnum convertToEntityAttribute(String dbData) {
		 return AtivoInativoEnum.getByValue(dbData);
	}
}
