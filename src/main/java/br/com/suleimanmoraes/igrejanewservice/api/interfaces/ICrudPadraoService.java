package br.com.suleimanmoraes.igrejanewservice.api.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;

import br.com.suleimanmoraes.igrejanewservice.api.enums.OpcaoTratarEnum;
import br.com.suleimanmoraes.igrejanewservice.api.exception.NegocioException;

public interface ICrudPadraoService<T> {
  List<T> findAll();

  T findById(Long id);

  T save(T objeto) throws NegocioException;

  Boolean deleteById(Long id) throws Exception;

  void validar(T objeto) throws Exception;

  Page<T> paginarComParemetros(Integer page, Integer size);

  T tratar(T objeto, OpcaoTratarEnum opcao);
}
