package com.challenge.tenpo.adapter.jdbc.model;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApiCallJdbcModelMapper implements RowMapper<ApiCallJdbcModel> {


    @Override
    public ApiCallJdbcModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ApiCallJdbcModel.builder()
                .id(rs.getLong("id"))
                .endpoint(rs.getString("endpoint"))
                .httpMethod(rs.getString("http_method"))
                .request(rs.getString("request"))
                .response(rs.getString("response"))
                .creationDate(rs.getTimestamp("creation_date").toLocalDateTime())
                .build();
    }
}
