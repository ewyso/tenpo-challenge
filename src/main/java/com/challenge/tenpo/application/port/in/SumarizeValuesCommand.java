package com.challenge.tenpo.application.port.in;

import com.challenge.tenpo.domain.NumbersWithPercentage;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

public interface SumarizeValuesCommand {

    NumbersWithPercentage sumarizeValues(Values values);

    @Value
    @Builder
    @ToString
    class Values {
        Long firstValue;
        Long secondValue;
    }

}
