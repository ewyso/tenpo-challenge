package com.challenge.tenpo.adapter.cache.model;


import com.challenge.tenpo.domain.NumbersWithPercentage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultCacheModel {
    Long firstValue;
    Long secondeValue;
    Long result;

    public static NumbersWithPercentage toDomain(ResultCacheModel model){
        return NumbersWithPercentage.builder()
                .firstNumber(model.getFirstValue())
                .secondNumber(model.getSecondeValue())
                .sumarizedValue(model.getResult())
                .build();
    }

}
