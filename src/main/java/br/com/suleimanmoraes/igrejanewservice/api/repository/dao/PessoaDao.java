package br.com.suleimanmoraes.igrejanewservice.api.repository.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterPessoaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.listagem.PessoaListagemDto;
import br.com.suleimanmoraes.igrejanewservice.api.enums.FiltroDizimistaEnum;

@Repository
public class PessoaDao {

	private static final Log logger = LogFactory.getLog(PessoaDao.class);

	private static final String SQL_CONSULTA_FILTRO_JOIN_USUARIO = " LEFT JOIN usuario usuario ON usuario.id = pessoa.id_usuario";
	private static final String SQL_CONSULTA_FILTRO_JOIN_CARGO = " LEFT JOIN cargo cargo ON cargo.id = pessoa.id_cargo";
	private static final String SQL_CONSULTA_FILTRO_JOIN_ENTRADA = " LEFT JOIN entrada entrada ON entrada.id_pessoa = pessoa.id";
	private static final String SQL_CONSULTA_FILTRO_FROM = "FROM pessoa pessoa" + SQL_CONSULTA_FILTRO_JOIN_USUARIO
			+ SQL_CONSULTA_FILTRO_JOIN_CARGO + SQL_CONSULTA_FILTRO_JOIN_ENTRADA;
	private static final String SQL_CONSULTA_FILTRO_GROUP = "GROUP BY pessoa.id, usuario.login, cargo.id ORDER BY pessoa.nome, pessoa.id ASC";

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<PessoaListagemDto> findByFilter(FilterPessoaDto filtro) {
		try {
			StringBuilder sql = new StringBuilder("SELECT");
			sql.append(" pessoa.id,");
			sql.append(" pessoa.nome,");
			sql.append(" usuario.login,");
			sql.append(" pessoa.telefone,");
			sql.append(" cargo.nome AS cargoNome,");
			sql.append(" cargo.id AS cargoId,");
			sql.append(" pessoa.id_igreja AS igrejaId,");
			sql.append(" pessoa.ativo");
			sql.append(" ");
			return getQueryByFilter(filtro, sql.toString(), SQL_CONSULTA_FILTRO_GROUP,
					PessoaListagemDto.PESSOA_LISTAGEM_DTO_MAPPING).setMaxResults(filtro.getSize())
							.setFirstResult(filtro.getSize() * filtro.getPage()).getResultList();
		} catch (Exception e) {
			logger.warn("findByFilter " + e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<PessoaListagemDto> findByFilterDizimista(FilterPessoaDto filtro) {
		try {
			StringBuilder sql = new StringBuilder("SELECT");
			sql.append(" pessoa.id,");
			sql.append(" pessoa.nome,");
			sql.append(" entrada.data_entrada AS dataEntrada,");
			sql.append(" entrada.valor AS valorDizimo");
			sql.append(" ");
			return getQueryByFilter(filtro, sql.toString(), "GROUP BY pessoa.id, entrada.id ORDER BY pessoa.nome",
					PessoaListagemDto.PESSOA_LISTAGEM_DIZIMISTA_DTO_MAPPING).setMaxResults(filtro.getSize())
							.setFirstResult(filtro.getSize() * filtro.getPage()).getResultList();
		} catch (Exception e) {
			logger.warn("findByFilter " + e.getMessage());
		}
		return null;
	}

	public Integer countByFilter(FilterPessoaDto filtro) {
		try {
			return Integer.valueOf(getQueryByFilter(filtro, "SELECT COUNT(DISTINCT(pessoa.id)) ", "", null)
					.getSingleResult().toString());
		} catch (Exception e) {
			logger.warn("findByFilter " + e.getMessage());
		}
		return 0;
	}

	private Query getQueryByFilter(FilterPessoaDto filtro, String sql, String complemento, String mappingName)
			throws Exception {
		try {
			Map<String, Object> mapa = new HashMap<>();
			StringBuilder tudo = new StringBuilder(sql);
			tudo.append(SQL_CONSULTA_FILTRO_FROM);
			tudo.append(" WHERE pessoa.id != 1");// Nunca mostrar o root
			if (filtro.getIgrejaId() != null && filtro.getIgrejaId() > 0) {
				tudo.append(" AND pessoa.id_igreja = :igrejaId");
				mapa.put("igrejaId", filtro.getIgrejaId());
			}
			if (filtro.getId() != null && filtro.getId() > 0) {
				tudo.append(" AND pessoa.id = :id");
				mapa.put("id", filtro.getId());
			}
			if (filtro.getCargoId() != null && filtro.getCargoId() > 0) {
				tudo.append(" AND cargo.id = :cargoId");
				mapa.put("cargoId", filtro.getCargoId());
			}
			if (filtro.getAtivo() != null) {
				tudo.append(" AND pessoa.ativo = :ativo");
				mapa.put("ativo", filtro.getAtivo().getValue());
			}
			if (StringUtils.hasLength(filtro.getNome())) {
				tudo.append(" AND UPPER(pessoa.nome) LIKE :nome");
				mapa.put("nome", "%" + filtro.getNome().toUpperCase() + "%");
			}
			if (StringUtils.hasLength(filtro.getLogin())) {
				tudo.append(" AND UPPER(usuario.login) LIKE :login");
				mapa.put("login", "%" + filtro.getLogin().toUpperCase() + "%");
			}
			if (StringUtils.hasLength(filtro.getCpf())) {
				tudo.append(" AND pessoa.cpf = :cpf");
				mapa.put("cpf", filtro.getCpf());
			}
			if (filtro.getFiltroDizimistaEnum() != null) {
				FiltroDizimistaEnum.TODOS.equals(filtro.getFiltroDizimistaEnum());
				final String mesAno = DateFormatUtils.format(new Date(), "yyyy-MM") + "%";
				tudo.append(" AND ((");
				if (!FiltroDizimistaEnum.TODOS.equals(filtro.getFiltroDizimistaEnum())) {
					tudo.append(" entrada.id_tipo_entrada = :tipoEntradaId AND pessoa.id");
					tudo.append(FiltroDizimistaEnum.NAO_DEVOLVEU.equals(filtro.getFiltroDizimistaEnum()) ? " NOT" : "");
					tudo.append(" IN (SELECT ent.id_pessoa FROM entrada ent");
					tudo.append(" WHERE ent.id_tipo_entrada = :tipoEntradaId");
					tudo.append(" AND ent.id_pessoa IS NOT NULL AND CONCAT(ent.data_entrada, '') LIKE :mesAno) AND");
					mapa.put("mesAno", mesAno);
					mapa.put("tipoEntradaId", 1l);
				}
				tudo.append(" (SELECT MAX(en.data_entrada) FROM entrada en WHERE en.id_pessoa = pessoa.id)");
				tudo.append(" = entrada.data_entrada)");
				if (!FiltroDizimistaEnum.DEVOLVEU.equals(filtro.getFiltroDizimistaEnum())) {
					tudo.append(" OR entrada.id IS NULL");
				}
				tudo.append(")");
			}
			tudo.append(" ").append(complemento);
			Query query = null;
			if (mappingName == null) {
				query = entityManager.createNativeQuery(tudo.toString());
			} else {
				query = entityManager.createNativeQuery(tudo.toString(), mappingName);
			}
			for (String key : mapa.keySet()) {
				query.setParameter(key, mapa.get(key));
			}
			return query;
		} catch (Exception e) {
			logger.warn("getQueryByFilter " + e.getMessage());
			throw e;
		}
	}
}
