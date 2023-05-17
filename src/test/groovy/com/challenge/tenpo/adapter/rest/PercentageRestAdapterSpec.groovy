package com.challenge.tenpo.adapter.rest

import com.challenge.tenpo.adapter.rest.exception.PercentageRestException
import com.challenge.tenpo.config.ExternalApiConfigurationProperties
import com.challenge.tenpo.domain.ComputedValue
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static com.challenge.tenpo.config.ErrorCode.PERCENTAGE_SERVICE_ERROR

class PercentageRestAdapterSpec extends Specification {

    RestTemplate restTemplate = Mock(RestTemplate)
    ExternalApiConfigurationProperties config = Mock(ExternalApiConfigurationProperties)

    PercentageRestAdapter adapter = new PercentageRestAdapter(restTemplate, config)


    def "Cuando shoudlBeMocked sea true no debera ejecutar un llamado rest"(){
        given:
        def firstValue = 1L
        def secondtValue = 1L

        config.shouldBeMocked >> true

        0 * restTemplate.exchange(_, _, _, _, _)

        when:
        def result = adapter.obtainSumarizedPercentage(firstValue, secondtValue)

        then:
        result instanceof ComputedValue

    }


    def "Cuando shoudlBeMocked sea false el valor sera obtenido del api externa"(){
        given:
        def firstValue = 1L
        def secondtValue = 1L

        config.shouldBeMocked >> false
        config.uri >> "http://a.valid.url/"

        1 * restTemplate.exchange(_, _, _, _, _) >> ResponseEntity.of(Optional.of(10L))

        when:
        def result = adapter.obtainSumarizedPercentage(firstValue, secondtValue)

        then:
        result.value == 10L
        result instanceof ComputedValue

    }


    def "Cuando shoudlBeMocked sea false y se produzca un error en el API externa debera arrojar exception"(){
        given:
        def firstValue = 1L
        def secondtValue = 1L

        config.shouldBeMocked >> false
        config.uri >> "http://a.valid.url/"

        1 * restTemplate.exchange(_, _, _, _, _) >> { throw new PercentageRestException(PERCENTAGE_SERVICE_ERROR) }

        when:
        adapter.obtainSumarizedPercentage(firstValue, secondtValue)

        then:
        thrown(PercentageRestException)

    }

}
