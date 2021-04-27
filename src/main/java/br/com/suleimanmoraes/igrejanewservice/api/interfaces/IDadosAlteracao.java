package br.com.suleimanmoraes.igrejanewservice.api.interfaces;

import java.util.Date;

public interface IDadosAlteracao {
  
  Date getDataCadastro();
  
  Date getDataAlteracao();

  Long getIdUsuarioCadastro();
  
  Long getIdUsuarioAlteracao();
  
  void setDataCadastro(Date dataCadastro);

  void setDataAlteracao(Date dataAlteracao);
  
  void setIdUsuarioAlteracao(Long idUsuarioAlteracao);

  void setIdUsuarioCadastro(Long idUsuarioCadastro);
  
  void prePersist();
  
  void preUpdate();

  default void prePersistAndUpdate() {
    final Date hoje = new Date();
    setDataCadastro(getDataCadastro() == null ? hoje : getDataCadastro());
    setDataAlteracao(hoje);
  }
}
