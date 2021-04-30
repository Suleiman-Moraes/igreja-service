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
import org.springframework.util.StringUtils;

import br.com.suleimanmoraes.igrejanewservice.api.dto.EntradaInformacaoDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.EntradaTipoEntradaInformacaoDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.GraficoGroupDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.filter.FilterEntradaDto;
import br.com.suleimanmoraes.igrejanewservice.api.dto.listagem.EntradaListagemDto;

@Repository
public class EntradaDao {

	private static final Log logger = LogFactory.getLog(EntradaDao.class);

	private static final String SQL_CONSULTA_FILTRO_JOIN_FORMAPAGAMENTO = " LEFT JOIN forma_pagamento formaPagamento ON formaPagamento.id = entrada.id_forma_pagamento";
	private static final String SQL_CONSULTA_FILTRO_JOIN_PESSOA = " LEFT JOIN pessoa pessoa ON pessoa.id = entrada.id_pessoa";
	private static final String SQL_CONSULTA_FILTRO_JOIN_TIPOENTRADA = " LEFT JOIN tipo_entrada tipoEntrada ON tipoEntrada.id = entrada.id_tipo_entrada";
	private static final String SQL_CONSULTA_FILTRO_FROM = "FROM entrada entrada"
			+ SQL_CONSULTA_FILTRO_JOIN_FORMAPAGAMENTO + SQL_CONSULTA_FILTRO_JOIN_PESSOA
			+ SQL_CONSULTA_FILTRO_JOIN_TIPOENTRADA;
	private static final String SQL_CONSULTA_FILTRO_GROUP = "GROUP BY entrada.id, formaPagamento.nome, tipoEntrada.id, pessoa.id ORDER BY entrada.id DESC";

	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<EntradaListagemDto> findByFilter(FilterEntradaDto filtro) {
		try {
			StringBuilder sql = new StringBuilder("SELECT");
			sql.append(" entrada.id,");
			sql.append(" entrada.data_entrada AS dataEntrada,");
			sql.append(" entrada.valor, entrada.nome,");
			sql.append(" formaPagamento.nome AS formaPagamentoNome,");
			sql.append(" entrada.id_igreja AS igrejaId,");
			sql.append(" tipoEntrada.nome AS tipoEntradaNome,");
			sql.append(" tipoEntrada.id AS tipoEntradaId,");
			sql.append(" pessoa.nome AS pessoaNome,");
			sql.append(" pessoa.id AS pessoaId,");
			sql.append(" entrada.ativo");
			sql.append(" ");
			return getQueryByFilter(filtro, sql.toString(), SQL_CONSULTA_FILTRO_GROUP,
					EntradaListagemDto.ENTRADA_LISTAGEM_DTO_MAPPING).setMaxResults(filtro.getSize())
							.setFirstResult(filtro.getSize() * filtro.getPage()).getResultList();
		} catch (Exception e) {
			logger.warn("findByFilter " + e.getMessage());
		}
		return null;
	}

	public EntradaInformacaoDto getInformacao(FilterEntradaDto filtro) {
		try {
			StringBuilder sql = new StringBuilder("SELECT");
			sql.append(" COUNT(entrada.id) AS totalRegistros,");
			sql.append(" SUM(entrada.valor) AS valorTotal,");
			sql.append(" AVG(entrada.valor) AS media");
			sql.append(" ");
			return (EntradaInformacaoDto) getQueryByFilter(filtro, sql.toString(), "",
					EntradaInformacaoDto.ENTRADA_INFORMACAO_DTO_MAPPING).getSingleResult();
		} catch (Exception e) {
			logger.warn("findByFilter " + e.getMessage());
		}
		return new EntradaInformacaoDto();
	}

	@SuppressWarnings("unchecked")
	public List<EntradaTipoEntradaInformacaoDto> getTipoEntradaInformacoes(FilterEntradaDto filtro) {
		try {
			StringBuilder sql = new StringBuilder("SELECT");
			sql.append(" COUNT(entrada.id_tipo_entrada) AS totalRegistros,");
			sql.append(" SUM(entrada.valor) AS valorTotal,");
			sql.append(" AVG(entrada.valor) AS media,");
			sql.append(" entrada.id_tipo_entrada AS tipoEntradaId,");
			sql.append(" tipoEntrada.nome AS tipoEntradaNome");
			sql.append(" ");
			return getQueryByFilter(filtro, sql.toString(),
					"GROUP BY entrada.id_tipo_entrada, tipoEntrada.nome ORDER BY tipoEntrada.nome",
					EntradaTipoEntradaInformacaoDto.ENTRADA_TIPO_ENTRADA_INFORMACAO_DTO_MAPPING).getResultList();
		} catch (Exception e) {
			logger.warn("findByFilter " + e.getMessage());
		}
		return null;
	}

	public Integer countByFilter(FilterEntradaDto filtro) {
		try {
			return Integer.valueOf(getQueryByFilter(filtro, "SELECT COUNT(DISTINCT(entrada.id)) ", "", null)
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
			sql.append(" SUM(e.valor) AS valor,");
			sql.append(" DATE_PART('MONTH', e.data_entrada) AS mes");
			sql.append(" FROM entrada e");
			sql.append(" WHERE DATE_PART('YEAR', e.data_entrada) = :ano");
			sql.append(" GROUP BY mes ORDER BY mes ASC");
			return entityManager.createNativeQuery(sql.toString(), GraficoGroupDto.GRAFICO_GROUP_DTO_MAPPING)
					.setParameter("ano", ano).getResultList();
		} catch (Exception e) {
			logger.warn("somaValorMensalPorAno " + e.getMessage());
			return new LinkedList<>();
		}
	}

	private Query getQueryByFilter(FilterEntradaDto filtro, String sql, String complemento, String mappingName)
			throws Exception {
		try {
			Map<String, Object> mapa = new HashMap<>();
			StringBuilder tudo = new StringBuilder(sql);
			tudo.append(SQL_CONSULTA_FILTRO_FROM);
			tudo.append(" WHERE 1 = 1");
			if (filtro.getIgrejaId() != null && filtro.getIgrejaId() > 0) {
				tudo.append(" AND entrada.id_igreja = :igrejaId");
				mapa.put("igrejaId", filtro.getIgrejaId());
			}
			if (filtro.getId() != null && filtro.getId() > 0) {
				tudo.append(" AND entrada.id = :id");
				mapa.put("id", filtro.getId());
			}
			if (filtro.getTipoEntradaId() != null && filtro.getTipoEntradaId() > 0) {
				tudo.append(" AND tipoEntrada.id = :tipoEntradaId");
				mapa.put("tipoEntradaId", filtro.getTipoEntradaId());
			}
			if (filtro.getAtivo() != null) {
				tudo.append(" AND entrada.ativo = :ativo");
				mapa.put("ativo", filtro.getAtivo().getValue());
			}
			if (StringUtils.hasLength(filtro.getNomePessoa())) {
				tudo.append(" AND UPPER(pessoa.nome) LIKE :nome");
				mapa.put("nome", "%" + filtro.getNomePessoa().toUpperCase() + "%");
			}
			if (filtro.getMesAno() != null) {
				final String mesAno = DateFormatUtils.format(filtro.getMesAno(), "yyyy-MM") + "%";
				tudo.append(" AND CONCAT(entrada.data_entrada, '') LIKE :mesAno");
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
