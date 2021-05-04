package br.com.suleimanmoraes.igrejanewservice.api.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.suleimanmoraes.igrejanewservice.api.enums.AtivoInativoEnum;
import br.com.suleimanmoraes.igrejanewservice.api.model.ItemMenu;

public interface ItemMenuRepository extends JpaRepository<ItemMenu, Long> {

	@Query(value = "SELECT new ItemMenu(i.id, i.nome, i.icon, i.url) FROM ItemMenu i " + 
			"LEFT JOIN i.permissoes p LEFT JOIN p.usuarios u " + 
			"WHERE i.menu.id = ?3 AND i.ativo = ?1 AND (u.id = ?2 OR p.id IS NULL) ")
	Set<ItemMenu> findByAtivoAndPermissoesUsuariosIdAndMenuId(AtivoInativoEnum ativo, Long usuarioId, Long menuId);
}
