package com.challenge.tenpo.application.usecase;

import com.challenge.tenpo.application.port.in.SumarizeValuesCommand;
import com.challenge.tenpo.application.port.out.PercentageRepository;
import com.challenge.tenpo.application.port.out.ResultsRepository;
import com.challenge.tenpo.domain.ComputedValue;
import com.challenge.tenpo.domain.NumbersWithPercentage;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SumarizeValuesUseCase implements SumarizeValuesCommand {

    private final PercentageRepository percentageRepository;
    private final ResultsRepository resultsRepository;

    public SumarizeValuesUseCase(PercentageRepository percentageRepository, ResultsRepository resultsRepository) {
        this.percentageRepository = percentageRepository;
        this.resultsRepository = resultsRepository;
    }

    @Override
    @Retryable(maxAttempts = 3, backoff =  @Backoff(delay = 100))
    public NumbersWithPercentage sumarizeValues(Values values) {
        log.info("Computando suma de porcentaje para valores: {}", values );
        val cacheKey = buildKey(values);
        val cachedDomain = getFromCache(cacheKey);

        if (cachedDomain != null) {
            log.info("Se encontro el valor computado en cache: {}", cachedDomain);
            return cachedDomain;
        } else {
            log.info("Se procede a computar valores con servicio externo");
            val response = percentageRepository.obtainSumarizedPercentage(
                    values.getFirstValue(),
                    values.getSecondValue()
            );

            val domain = buildNumbersWithPercentage(values, response);

            resultsRepository.save(cacheKey, domain);

            return domain;
        }
    }

    private NumbersWithPercentage getFromCache(String cacheKey) {
        return resultsRepository.get(cacheKey);
    }

    private NumbersWithPercentage buildNumbersWithPercentage(Values values, ComputedValue computedValue) {
        return NumbersWithPercentage.builder()
                .firstNumber(values.getFirstValue())
                .secondNumber(values.getSecondValue())
                .sumarizedValue(computedValue.getValue())
                .build();
    }

    private String buildKey(Values values) {
        return values.getFirstValue().toString().concat(":").concat(values.getSecondValue().toString());
    }


}
