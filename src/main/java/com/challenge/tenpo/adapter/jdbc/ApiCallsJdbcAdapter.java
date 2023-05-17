package com.challenge.tenpo.adapter.jdbc;

import com.challenge.tenpo.adapter.jdbc.exception.EntityNotFoundJdbcException;
import com.challenge.tenpo.adapter.jdbc.model.ApiCallJdbcModel;
import com.challenge.tenpo.adapter.jdbc.model.ApiCallJdbcModelMapper;
import com.challenge.tenpo.application.exception.AdapterException;
import com.challenge.tenpo.application.port.out.ApiCallsRepository;
import com.challenge.tenpo.domain.ApiCallModel;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.challenge.tenpo.config.ErrorCode.ENTITY_NOT_FOUND_ERROR;
import static com.challenge.tenpo.config.ErrorCode.JDBC_ERROR;

@Component
@Slf4j
public class ApiCallsJdbcAdapter implements ApiCallsRepository {

    private static final String PATH_INSERT_API_CALL = "sql/insertApiCall.sql";
    private static final String PATH_GET_ALL_API_CALLS = "sql/getAllApiCalls.sql";
    private static final String PATH_GET_BY_ID = "sql/getApiCallById.sql";
    private static final String PATH_COUNT_API_CALL = "sql/countApiCalls.sql";

    private final JdbcTemplate jdbcTemplate;
    private final String insertApiCall;
    private final String getAll;
    private final String getById;
    private final String countApiCallsQuery;

    public ApiCallsJdbcAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertApiCall = SqlReader.readSql(PATH_INSERT_API_CALL);
        this.countApiCallsQuery = SqlReader.readSql(PATH_COUNT_API_CALL);
        this.getAll = SqlReader.readSql(PATH_GET_ALL_API_CALLS);
        this.getById = SqlReader.readSql(PATH_GET_BY_ID);
    }

    @Override
    public void save(String endpoint, String httpMethod, String request, String response) {
        log.info("Guardando API Call para endpoint: {}. Request: {}", endpoint, request);
        try {
            this.jdbcTemplate.update(insertApiCall, endpoint, httpMethod, request, response);
        } catch (DataAccessException e) {
            log.error("Error al insertar API Call para endpoint: {}", endpoint, e);
            throw new AdapterException(JDBC_ERROR);
        }
    }

    @Override
    public Page<ApiCallModel> getAll(Integer page, Integer size) {
        log.info("Obteniendo el total de request para paginar");
        int totalElements = jdbcTemplate.queryForObject(countApiCallsQuery, (rs, rowNum) -> rs.getInt(1));
        log.info("total de api calls: {}", totalElements);

        String selectQuery = buildSelectQuery(this.getAll, page, size);

        Pageable pageable = PageRequest.of(page, size);

        val results = jdbcTemplate.query(selectQuery, new ApiCallJdbcModelMapper())
                .stream()
                .map(ApiCallJdbcModel::toDomain).collect(Collectors.toList());

        return new PageImpl<>(results, pageable, totalElements);
    }

    @Override
    public ApiCallModel getById(Long id) {
        log.info("Buscando ApiCall para id: {}", id);

        List<ApiCallJdbcModel> jdbcModel = this.jdbcTemplate.query(
                this.getById,
                new Object[]{id},
                new ApiCallJdbcModelMapper()
        );

        return jdbcModel.stream()
                .peek(it -> log.info("Entidad asociadad al id: {}. Entidad: {}", id, it))
                .findFirst()
                .map(ApiCallJdbcModel::toDomain)
                .orElseThrow(() -> new EntityNotFoundJdbcException(ENTITY_NOT_FOUND_ERROR));
    }

    public String buildSelectQuery(String query, int page, int size) {
        StringBuilder sb = new StringBuilder(query);

        sb.append(" ORDER BY creation_date DESC");
        int offset = page * size;
        sb.append(" OFFSET ").append(offset);
        sb.append(" FETCH FIRST ").append(size).append(" ROWS ONLY");

        return sb.toString();
    }

}
