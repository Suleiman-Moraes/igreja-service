package br.com.suleimanmoraes.igrejanewservice.api.repository.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import br.com.suleimanmoraes.igrejanewservice.api.dto.GraficoGroupDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.SaidaInformacaoDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterSaidaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.listagem.SaidaListagemDto;

@Repository
public class SaidaDao {

	private static final Log logger = LogFactory.getLog(SaidaDao.class);

	private static final String SQL_CONSULTA_FILTRO_JOIN_FORMAPAGAMENTO = " LEFT JOIN forma_pagamento formaPagamento ON formaPagamento.id = saida.id_forma_pagamento";
	private static final String SQL_CONSULTA_FILTRO_FROM = "FROM saida saida" + SQL_CONSULTA_FILTRO_JOIN_FORMAPAGAMENTO;
	private static final String SQL_CONSULTA_FILTRO_GROUP = "GROUP BY saida.id, formaPagamento.nome ORDER BY saida.id DESC";

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<SaidaListagemDto> findByFilter(FilterSaidaDto filtro) {
		try {
			StringBuilder sql = new StringBuilder("SELECT");
			sql.append(" saida.id,");
			sql.append(" saida.data_saida AS dataSaida,");
			sql.append(" saida.valor, saida.nome,");
			sql.append(" formaPagamento.nome AS formaPagamentoNome,");
			sql.append(" saida.id_igreja AS igrejaId,");
			sql.append(" saida.ativo");
			sql.append(" ");
			return getQueryByFilter(filtro, sql.toString(), SQL_CONSULTA_FILTRO_GROUP,
					SaidaListagemDto.SAIDA_LISTAGEM_DTO_MAPPING).setMaxResults(filtro.getSize())
							.setFirstResult(filtro.getSize() * filtro.getPage()).getResultList();
		} catch (Exception e) {
			logger.warn("findByFilter " + e.getMessage());
		}
		return null;
	}

	public SaidaInformacaoDto getInformacao(FilterSaidaDto filtro) {
		try {
			StringBuilder sql = new StringBuilder("SELECT");
			sql.append(" COUNT(saida.id) AS totalRegistros,");
			sql.append(" SUM(saida.valor) AS valorTotal,");
			sql.append(" AVG(saida.valor) AS media");
			sql.append(" ");
			return (SaidaInformacaoDto) getQueryByFilter(filtro, sql.toString(), "",
					SaidaInformacaoDto.SAIDA_INFORMACAO_DTO_MAPPING).getSingleResult();
		} catch (Exception e) {
			logger.warn("findByFilter " + e.getMessage());
		}
		return new SaidaInformacaoDto();
	}

	public Integer countByFilter(FilterSaidaDto filtro) {
		try {
			return Integer.valueOf(getQueryByFilter(filtro, "SELECT COUNT(DISTINCT(saida.id)) ", "", null)
					.getSingleResult().toString());
		} catch (Exception e) {
			logger.warn("findByFilter " + e.getMessage());
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	public List<GraficoGroupDto> somaValorMensalPorAno(Integer ano) {
		try {
			StringBuilder sql = new StringBuilder("SELECT");
			sql.append(" SUM(s.valor) AS valor,");
			sql.append(" DATE_PART('MONTH', s.data_saida) AS mes");
			sql.append(" FROM saida s");
			sql.append(" WHERE DATE_PART('YEAR', s.data_saida) = :ano");
			sql.append(" GROUP BY mes ORDER BY mes ASC");
			return entityManager.createNativeQuery(sql.toString(), GraficoGroupDto.GRAFICO_GROUP_DTO_MAPPING)
					.setParameter("ano", ano).getResultList();
		} catch (Exception e) {
			logger.warn("somaValorMensalPorAno " + e.getMessage());
			return new LinkedList<>();
		}
	}

	private Query getQueryByFilter(FilterSaidaDto filtro, String sql, String complemento, String mappingName)
			throws Exception {
		try {
			Map<String, Object> mapa = new HashMap<>();
			StringBuilder tudo = new StringBuilder(sql);
			tudo.append(SQL_CONSULTA_FILTRO_FROM);
			tudo.append(" WHERE 1 = 1");
			if (filtro.getIgrejaId() != null && filtro.getIgrejaId() > 0) {
				tudo.append(" AND saida.id_igreja = :igrejaId");
				mapa.put("igrejaId", filtro.getIgrejaId());
			}
			if (filtro.getId() != null && filtro.getId() > 0) {
				tudo.append(" AND saida.id = :id");
				mapa.put("id", filtro.getId());
			}
			if (filtro.getAtivo() != null) {
				tudo.append(" AND saida.ativo = :ativo");
				mapa.put("ativo", filtro.getAtivo().getValue());
			}
			if (filtro.getMesAno() != null) {
				final String mesAno = DateFormatUtils.format(filtro.getMesAno(), "yyyy-MM") + "%";
				tudo.append(" AND CONCAT(saida.data_saida, '') LIKE :mesAno");
				mapa.put("mesAno", mesAno);
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
