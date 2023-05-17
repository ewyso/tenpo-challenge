package com.challenge.tenpo.application.usecase

import com.challenge.tenpo.adapter.rest.exception.PercentageRestException
import com.challenge.tenpo.application.port.in.SumarizeValuesCommand
import com.challenge.tenpo.application.port.out.PercentageRepository
import com.challenge.tenpo.application.port.out.ResultsRepository
import com.challenge.tenpo.config.ErrorCode
import com.challenge.tenpo.domain.ComputedValue
import com.challenge.tenpo.domain.NumbersWithPercentage
import spock.lang.Specification

class SumarizeValuesUseCaseSpec extends Specification {

    PercentageRepository percentageRepository = Mock(PercentageRepository)
    ResultsRepository resultsRepository = Mock(ResultsRepository)

    SumarizeValuesUseCase useCase = new SumarizeValuesUseCase(percentageRepository, resultsRepository)


    static aValidValues() {
        return SumarizeValuesCommand.Values.builder()
                .firstValue(10L)
                .secondValue(15L).build()
    }

    static aValidNumbersWithPercentageDomain() {
        return NumbersWithPercentage.builder()
                .firstNumber(10L)
                .secondNumber(15L)
                .sumarizedValue(65L).build()
    }


    def "sumarizeValues - Dado un Values valido, debera retornar un domain con el valor computado"() {
        given:
        def values = aValidValues()
        def computedValue = ComputedValue.builder().value(150L).build()
        1 * resultsRepository.get(_) >> null
        1 * resultsRepository.save(_, _) >> {}

        percentageRepository.obtainSumarizedPercentage(values.firstValue, values.secondValue) >> computedValue

        when:
        def result = useCase.sumarizeValues(values)

        then:
        result.sumarizedValue == 150L
        noExceptionThrown()
    }

    def "sumarizeValues - Dado un valor que esta presente en cache, debera deolverlo desde el mismo"() {
        given:
        def values = aValidValues()
        def aCachedResult = aValidNumbersWithPercentageDomain()

        1 * resultsRepository.get(_) >> aCachedResult
        0 * resultsRepository.save(_)
        0 * percentageRepository.obtainSumarizedPercentage(_, _)

        when:
        def result = useCase.sumarizeValues(values)

        then:
        result.sumarizedValue == 65L
        noExceptionThrown()

    }


    def "sumarizeValues - Cuando se produzca un error se reintentara el flujo hasta 3 veces"() {
        given:
        def values = aValidValues()
        def computedValue = ComputedValue.builder().value(150L).build()

        3 * resultsRepository.get(_) >>> null >> null >> null
        3 * percentageRepository.obtainSumarizedPercentage(_, _) >>>
                { throw new PercentageRestException(ErrorCode.PERCENTAGE_SERVICE_ERROR) } >>
                { throw new PercentageRestException(ErrorCode.PERCENTAGE_SERVICE_ERROR) } >>
                { throw new PercentageRestException(ErrorCode.PERCENTAGE_SERVICE_ERROR) }

        1 * resultsRepository.save(_) >> {} >> {} >> {}

        when:
        useCase.sumarizeValues(values)

        then:
        thrown(PercentageRestException)

    }


}
