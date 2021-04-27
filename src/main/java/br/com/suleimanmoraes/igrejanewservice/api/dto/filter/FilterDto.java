package br.com.suleimanmoraes.igrejanewservice.api.dto.filter;

import br.com.suleimanmoraes.igrejanewservice.api.interfaces.IFilterBasic;
import lombok.Data;

@Data
public class FilterDto implements IFilterBasic{
  private Integer page;
  
  private Integer size;
  
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
