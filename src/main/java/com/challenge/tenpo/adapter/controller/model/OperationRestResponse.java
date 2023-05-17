package com.challenge.tenpo.adapter.controller.model;


import com.challenge.tenpo.domain.NumbersWithPercentage;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OperationRestResponse {
    Long sumarizedValue;

    public static OperationRestResponse fromDomain(NumbersWithPercentage domain){
        return OperationRestResponse.builder()
                .sumarizedValue(domain.getSumarizedValue())
                .build();
    }
}
