package com.challenge.tenpo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumbersWithPercentage {
    Long firstNumber;
    Long secondNumber;
    Long sumarizedValue;
}
