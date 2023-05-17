package com.challenge.tenpo.application.usecase

import com.challenge.tenpo.application.exception.AdapterException
import com.challenge.tenpo.application.port.in.RegisterApiCallCommand
import com.challenge.tenpo.application.port.out.ApiCallsRepository
import com.challenge.tenpo.commons.JsonMapper
import spock.lang.Specification

import static com.challenge.tenpo.config.ErrorCode.JDBC_ERROR

class RegisterApiCallUseCaseSpec extends Specification {

    ApiCallsRepository repository = Mock(ApiCallsRepository)
    JsonMapper jsonMapper = Mock(JsonMapper)

    RegisterApiCallUseCase useCase = new RegisterApiCallUseCase(repository, jsonMapper)


    def "registerApiCall - Dado un command valido, debera performarse un guardado en repositorio"(){
        given:
        def command = aValidCommand()
        2 * jsonMapper.serialize(_) >> "validJsonString"
        1 * repository.save(command.endpoint, command.httpMethod, _, _) >> {}

        when:
        useCase.registerApiCall(command)

        then:
        noExceptionThrown()
    }

    def "registerApiCall - Dado un command valido, cuando se produzca una exception, no debera bloquear el flujo"(){
        given:
        def command = aValidCommand()
        2 * jsonMapper.serialize(_) >> "validJsonString"
        1 * repository.save(command.endpoint, command.httpMethod, _, _) >> { throw new AdapterException(JDBC_ERROR) }

        when:
        useCase.registerApiCall(command)

        then:
        noExceptionThrown()
    }


    static aValidCommand(){
        return RegisterApiCallCommand.Command.builder()
                    .endpoint("/api/v1/tenpo")
                    .httpMethod("POST")
                    .request("aValidRequest")
                    .response("aValidResponse").build()
    }

}
