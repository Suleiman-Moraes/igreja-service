package br.com.suleimanmoraes.igrejanewservice.api.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	@Query(value = "SELECT new Menu(m.id, m.nome) FROM Menu m " + 
			"INNER JOIN m.itemMenus i LEFT JOIN i.permissoes p LEFT JOIN p.usuarios u " + 
			"WHERE i.ativo = ?1 AND (u.id = ?2 OR p.id IS NULL)")
	Set<Menu> findByItemMenusAtivoAndItemMenusPermissoesUsuariosId(AtivoInativoEnum ativo, Long usuarioId);

	@Query(value = "SELECT new Menu(m.id, m.nome) FROM Menu m")
	List<Menu> findBy();
}
