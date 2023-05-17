package com.challenge.tenpo.application.port.out;

import com.challenge.tenpo.domain.ComputedValue;

public interface PercentageRepository {
    ComputedValue obtainSumarizedPercentage(Long firstValue, Long secondValue);
}
