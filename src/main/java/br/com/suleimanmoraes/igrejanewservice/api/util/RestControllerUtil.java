package br.com.suleimanmoraes.igrejanewservice.api.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.suleimanmoraes.igrejanewservice.api.enums.OpcaoTratarEnum;
import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;
import br.com.suleimanmoraes.igrejanewservice.api.interfaces.CrudPadraoService;
import br.com.suleimanmoraes.igrejanewservice.api.interfaces.IFilterBasic;

/**
 * 
 * @author suleiman-am
 *
 */
public abstract class RestControllerUtil {

	private static final Log logger = LogFactory.getLog(RestControllerUtil.class);

	/**
	 * 
	 * @param service
	 * @return
	 */
	public static <T> ResponseEntity<List<T>> findAllCompleto(CrudPadraoService<T> service) {
		List<T> listObject = service.findAll();
		logarInfo("findAll com sucesso");
		return ResponseEntity.ok(listObject);
	}

	/**
	 * 
	 * @param service
	 * @param id
	 * @return
	 */
	public static <T> ResponseEntity<T> findByIdCompleto(CrudPadraoService<T> service, long id) {
		T objeto = service.findById(id);
		objeto = service.tratar(objeto, OpcaoTratarEnum.MOSTRAR);
		logarInfo("findById com sucesso");
		return ResponseEntity.ok(objeto);
	}

	/**
	 * 
	 * @param service
	 * @param objeto
	 * @return
	 */
	public static <T> T save(CrudPadraoService<T> service, T objeto) {
		T objetoNovo = service.save(objeto);
		return objetoNovo;
	}

	/**
	 * 
	 * @param service
	 * @param objeto
	 * @return
	 * @throws Exception 
	 */
	public static <T> ResponseEntity<T> saveCompleto(CrudPadraoService<T> service, T objeto) throws Exception {
		service.validar(objeto);
		T objetoNovo = service.save(objeto);
		objetoNovo = service.tratar(objetoNovo, OpcaoTratarEnum.MOSTRAR);
		logarInfo("save com sucesso");
		return ResponseEntity.status(HttpStatus.OK).body(objetoNovo);
	}

	/**
	 * 
	 * @param service
	 * @param objeto
	 * @return
	 * @throws NegocioException 
	 */
	public static <T> ResponseEntity<T> updateCompleto(CrudPadraoService<T> service, T objeto) throws Exception {
		ValidacaoComumUtil.validarObjectAndId(objeto, "Id", 'o');
		return saveCompleto(service, objeto);
	}

	/**
	 * 
	 * @param service
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static <T> ResponseEntity<Boolean> deleteByIdCompleto(CrudPadraoService<T> service, long id)
			throws Exception {
		Boolean retorno = service.deleteById(id);
		logarInfo("deleteById com sucesso");
		return ResponseEntity.ok(retorno);
	}

	public static ResponseEntity<Page<?>> findByParams(CrudPadraoService<?> service, Integer page, Integer count) {
		Page<?> pagina = service.paginarComParemetros(page, count);
		logarInfo("findByParams com sucesso");
		return ResponseEntity.ok(pagina);
	}

	public static ResponseEntity<Page<?>> findByParams(CrudPadraoService<?> service, IFilterBasic filter) {
		return findByParams(service, filter.getPage(), filter.getSize());
	}

	public static void logarError(Class<?> classe) {
		logger.error("Error no retorno do controlador: " + classe.getName());
	}

	public static void logarError(Class<?> classe, String erro) {
		logger.error("Error no retorno do controlador: " + classe.getName());
		logger.error("Error no retorno do controlador: " + erro);
	}

	public static void logarInfo(Class<?> classe, String msg) {
		logger.info("Informação no controlador: " + classe.getName());
		logger.info(msg);
	}

	public static void logarInfo(String msg) {
		logger.info(msg);
	}

	public static void logarError(Class<?> classe, String erro, String metodo) {
		logger.error("Error no retorno do controlador: " + classe.getName());
		logger.error("Error no retorno do controlador: " + erro);
		logger.error("Error no retorno do controlador/metodo: " + metodo);
	}
}
