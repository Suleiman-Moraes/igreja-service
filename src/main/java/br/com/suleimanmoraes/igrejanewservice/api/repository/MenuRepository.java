package br.com.suleimanmoraes.igrejanewservice.api.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.suleimanmoraes.igrejanewservice.api.model.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

	@Query(nativeQuery = true, value = "SELECT * FROM menu m " + 
			"INNER JOIN item_menu i ON i.id_menu = m.id " + 
			"LEFT JOIN item_menu_permissao imp ON imp.id_item_menu = i.id " + 
			"LEFT JOIN usuario_permissao up ON up.id_permissao = imp.id_permissao " + 
			"WHERE i.ativo = ?1 AND (up.id_usuario = ?2 OR imp.id IS NULL) " +
			"GROUP BY m.id, i.id, imp.id, up.id")
	Set<Menu> findByItemMenusAtivoAndItemMenusPermissoesUsuariosId(String ativo, Long usuarioId);

	@Query(value = "SELECT new Menu(m.id, m.nome) FROM Menu m")
	List<Menu> findBy();
}
