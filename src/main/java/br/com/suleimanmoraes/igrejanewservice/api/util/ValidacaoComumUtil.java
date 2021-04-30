package br.com.suleimanmoraes.igrejanewservice.api.util;

import java.util.LinkedList;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;

/**
 * 
 * @author Suleiman Moraes
 *
 */
public abstract class ValidacaoComumUtil {

	public static List<String> validarString(String texto, String nome, char fim, List<String> erros) {
		if (!StringUtils.hasText(texto)) {
			erros.add(String.format("%s deve ser informad%s.", nome, fim));
		}
		return erros;
	}

	public static List<String> validarString(String texto, String nome, char fim, List<String> erros, Integer qtdChar) {
		if (!StringUtils.hasText(texto)) {
			erros.add(String.format("%s deve ser informad%s.", nome, fim));
		} else if (texto.length() > qtdChar) {
			erros.add(String.format("\"%s\" excedeu a quantidade máxima de caracteres de %s.", nome, qtdChar));
		}
		return erros;
	}

	public static List<String> validarString(String texto, String nome, List<String> erros, Integer qtdChar) {
		if (StringUtils.hasText(texto) && texto.length() > qtdChar) {
			erros.add(String.format("\"%s\" excedeu a quantidade máxima de caracteres de %s.", nome, qtdChar));
		}
		return erros;
	}

	public static void validarString(String texto, String nome, char fim) throws NegocioException {
		if (!StringUtils.hasText(texto)) {
			throw new NegocioException(String.format("%s deve ser informad%s.", nome, fim));
		}
	}

	public static void validarString(String texto, String nome, char fim, Integer qtdChar) throws NegocioException {
		if (!StringUtils.hasText(texto)) {
			throw new NegocioException(String.format("%s deve ser informad%s.", nome, fim));
		} else if (texto.length() > qtdChar) {
			throw new NegocioException(
					String.format("\"%s\" excedeu a quantidade máxima de caracteres de %s.", nome, qtdChar));
		}
	}

	public static List<String> validarMaiorZero(Integer objeto, String nome, char fim, List<String> erros) {
		if (objeto != null && objeto <= 0) {
			erros.add(String.format("%s deve ser maior que zero.", nome));
		}
		return erros;
	}

	public static List<String> validarNotNullAndMaiorZero(Integer objeto, String nome, char fim, List<String> erros) {
		if (objeto == null) {
			erros.add(String.format("%s deve ser informad%s.", nome, fim));
		}
		erros = validarMaiorZero(objeto, nome, fim, erros);
		return erros;
	}

	public static List<String> validarNotNullAndMaiorZero(Double objeto, String nome, char fim, List<String> erros) {
		if (objeto == null) {
			erros.add(String.format("%s deve ser informad%s.", nome, fim));
		} else if (objeto <= 0) {
			erros.add(String.format("%s deve ser maior que zero.", nome));
		}
		return erros;
	}

	public static List<String> validarNotNullAndMaiorIgualZero(Double objeto, String nome, char fim,
			List<String> erros) {
		if (objeto == null) {
			erros.add(String.format("%s deve ser informad%s.", nome, fim));
		} else if (objeto < 0) {
			erros.add(String.format("%s deve ser maior ou igual zero.", nome));
		}
		return erros;
	}

	public static List<String> validarNotNullAndMaiorIgualZero(Integer objeto, String nome, char fim,
			List<String> erros) {
		if (objeto == null) {
			erros.add(String.format("%s deve ser informad%s.", nome, fim));
		} else if (objeto < 0) {
			erros.add(String.format("%s deve ser maior ou igual zero.", nome));
		}
		return erros;
	}

	public static List<String> validarObjectAndId(Object objeto, String nome, char fim, List<String> erros) throws Exception {
		try {
			if (objeto == null) {
				erros.add(String.format("%s deve ser informad%s.", nome, fim));
			} 
			else if (objeto.getClass().getMethod("getId").invoke(objeto) == null) {
				erros.add(String.format("%s deve ser informad%s.", nome, fim));
			}
			else if(Double.valueOf(objeto.getClass().getMethod("getId").invoke(objeto).toString()) <= 0) {
				erros.add(String.format("%s deve ser maior que zero.", nome));
			}
		} catch (NegocioException e) {
			erros.add(String.format("%s deve ser informad%s.", nome, fim));
		}
		return erros;
	}

	public static void validarObjectAndId(Object objeto, String nome, char fim) throws Exception {
		List<String> erros = new LinkedList<>();
		erros = validarObjectAndId(objeto, nome, fim, erros);
		if (!CollectionUtils.isEmpty(erros)) {
			throw new NegocioException(erros.get(0));
		}
	}

	public static List<String> validarNotNullAndMaiorZero(Long objeto, String nome, char fim, List<String> erros) {
		if (objeto == null) {
			erros.add(String.format("%s deve ser informad%s.", nome, fim));
		} else if (objeto <= 0) {
			erros.add(String.format("%s deve ser maior que zero.", nome));
		}
		return erros;
	}

	public static void validarBoolean(boolean objeto, String nome, char fim) throws NegocioException {
		if (!objeto) {
			throw new NegocioException(String.format("%s deve ser informad%s.", nome, fim));
		}
	}

	public static List<String> validarBoolean(boolean objeto, String nome, char fim, List<String> erros) {
		if (!objeto) {
			erros.add(String.format("%s deve ser informad%s.", nome, fim));
		}
		return erros;
	}

	public static List<String> validarNotNull(Object objeto, String nome, char fim, List<String> erros) {
		if (objeto == null) {
			erros.add(String.format("%s deve ser informad%s.", nome, fim));
		}
		return erros;
	}

	public static void validarNotNull(Object objeto, String nome, char fim) throws NegocioException {
		if (objeto == null) {
			throw new NegocioException(String.format("%s deve ser informad%s.", nome, fim));
		}
	}

	public static void validarNotNullAndMaiorZero(Integer objeto, String nome, char fim) throws NegocioException {
		if (objeto == null) {
			throw new NegocioException(String.format("%s deve ser informad%s.", nome, fim));
		} else if (objeto <= 0) {
			throw new NegocioException(String.format("%s deve ser maior que zero.", nome));
		}
	}

	public static void validarNotNullAndMaiorZero(Long objeto, String nome, char fim) throws NegocioException {
		if (objeto == null) {
			throw new NegocioException(String.format("%s deve ser informad%s.", nome, fim));
		} else if (objeto <= 0) {
			throw new NegocioException(String.format("%s deve ser maior que zero.", nome));
		}
	}

	public static void validarNotNullAndMaiorZero(Double objeto, String nome, char fim) throws NegocioException {
		if (objeto == null) {
			throw new NegocioException(String.format("%s deve ser informad%s.", nome, fim));
		} else if (objeto <= 0) {
			throw new NegocioException(String.format("%s deve ser maior que zero.", nome));
		}
	}
}
