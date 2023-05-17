package com.challenge.tenpo.adapter.jdbc.model;

import com.challenge.tenpo.domain.ApiCallModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiCallJdbcModel {
    private Long id;
    private String endpoint;
    private String httpMethod;
    private String request;
    private String response;
    private LocalDateTime creationDate;

    public ApiCallModel toDomain(){
        return ApiCallModel.builder()
                .endpoint(endpoint)
                .httpMethod(httpMethod)
                .request(request)
                .response(response)
                .build();
    }
}
