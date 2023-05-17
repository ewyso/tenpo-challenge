package com.challenge.tenpo.application.port.out;

import com.challenge.tenpo.domain.NumbersWithPercentage;

public interface ResultsRepository {

    void save(String key, NumbersWithPercentage numbersWithPercentage);

    NumbersWithPercentage get(String key);

}
