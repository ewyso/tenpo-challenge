package com.challenge.tenpo.adapter.rest;

import com.challenge.tenpo.adapter.rest.exception.PercentageRestException;
import com.challenge.tenpo.adapter.rest.exception.RestClientGenericException;
import com.challenge.tenpo.adapter.rest.handler.RestTemplateErrorHandler;
import com.challenge.tenpo.adapter.rest.model.PercentageRestRequest;
import com.challenge.tenpo.application.port.out.PercentageRepository;
import com.challenge.tenpo.config.ErrorCode;
import com.challenge.tenpo.config.ExternalApiConfigurationProperties;
import com.challenge.tenpo.domain.ComputedValue;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

import static com.challenge.tenpo.config.ErrorCode.PERCENTAGE_SERVICE_ERROR;

@Slf4j
@Component
public class PercentageRestAdapter implements PercentageRepository {

    private final RestTemplate restTemplate;
    private final ExternalApiConfigurationProperties config;

    public PercentageRestAdapter(RestTemplate restTemplate, ExternalApiConfigurationProperties config) {
        this.restTemplate = restTemplate;
        this.config = config;

        var errorHandler = new RestTemplateErrorHandler(
                Map.of(
                        HttpStatus.INTERNAL_SERVER_ERROR, new RestClientGenericException(ErrorCode.EXTERNAL_SERVICE_ERROR)
                )
        );

        this.restTemplate.setErrorHandler(errorHandler);
    }

    @Override
    public ComputedValue obtainSumarizedPercentage(Long firstValue, Long secondValue) {
        log.info("Ejecutando llamada a servicio externo para obtener porcentaje");
        if (config.getShouldBeMocked()) {
            val randomPercentage = processOperation(firstValue, secondValue);
            return buildResponse(randomPercentage);
        } else {
            val request = buildRequest(firstValue, secondValue);

            val response = Optional.ofNullable(restTemplate.exchange(
                    config.getUri(),
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    Long.class
            ).getBody()).orElseThrow( () -> new PercentageRestException(PERCENTAGE_SERVICE_ERROR));

            log.info("Porcentaje sumado obtenido del servicio: {}", response);

            return buildResponse(response);

        }

    }

    private ComputedValue buildResponse(Long sumarizedPercentage) {
        return ComputedValue.builder().value(sumarizedPercentage).build();
    }

    private PercentageRestRequest buildRequest(Long firstValue, Long secondValue) {
        return PercentageRestRequest.builder()
                .firstValue(firstValue)
                .secondValue(secondValue)
                .build();
    }

    private Long processOperation(Long firstValue, Long secondValue) {
        return (firstValue + secondValue) + (long) (Math.random() * 50 + 1);
    }
}
