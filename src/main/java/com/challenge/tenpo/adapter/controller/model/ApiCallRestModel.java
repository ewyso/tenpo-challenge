package com.challenge.tenpo.adapter.controller.model;

import com.challenge.tenpo.domain.ApiCallModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@AllArgsConstructor
@NoArgsConstructor
public class ApiCallRestModel {
    private String endpoint;
    private String httpMethod;
    private String request;
    private String response;

    public static ApiCallRestModel fromDomain(ApiCallModel model){
        return ApiCallRestModel.builder()
                .endpoint(model.getEndpoint())
                .httpMethod(model.getHttpMethod())
                .request(model.getRequest())
                .response(model.getResponse())
                .build();
    }
}
