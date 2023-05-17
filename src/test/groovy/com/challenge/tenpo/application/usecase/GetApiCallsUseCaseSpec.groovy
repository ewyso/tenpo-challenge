package com.challenge.tenpo.application.usecase

import com.challenge.tenpo.adapter.jdbc.exception.EntityNotFoundJdbcException
import com.challenge.tenpo.application.port.out.ApiCallsRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import spock.lang.Specification

import static com.challenge.tenpo.config.ErrorCode.ENTITY_NOT_FOUND_ERROR

import static com.challenge.tenpo.testData.ApiCallModelTestData.aValidApiCallModel

class GetApiCallsUseCaseSpec extends Specification {

    ApiCallsRepository repository = Mock(ApiCallsRepository)

    GetApiCallsUseCase useCase = new GetApiCallsUseCase(repository)

    def "execute - Dado parametors de busqueda validos, debera retornar un Page de ApiCallModel"(){
        given:
        def page = 1
        def size = 1
        def aListOfApiCalls = [aValidApiCallModel()]
        def aValidPageResponse = new PageImpl<>(aListOfApiCalls)

        repository.getAll(page, size) >> aValidPageResponse


        when:
        def response = useCase.execute(page, size)

        then:
        response.size() == 1
        noExceptionThrown()

    }

    def "execute - Dado parametors de busqueda validos, cuando el size sea mayor a 1,  debera retornar un Page de ApiCallModel valido"(){
        given:
        def page = 1
        def size = 3
        def aListOfApiCalls = [aValidApiCallModel(), aValidApiCallModel(), aValidApiCallModel() ]
        def aValidPageResponse = new PageImpl<>(aListOfApiCalls)

        repository.getAll(page, size) >> aValidPageResponse


        when: "Cuando se ejecute con un size de 3"
        def response = useCase.execute(page, size)

        then: "Debera devovler un Page con 3 elementos"
        response.size() == size
        noExceptionThrown()

    }


    def "getById - Dado un id valido, debera devolver un apiCall asociado"(){
        given:
        def id = 10L
        def jdbcResponse = aValidApiCallModel()
        repository.getById(id) >>  jdbcResponse

        when:
        def response = useCase.getById(id)

        then:
        with(response){
            endpoint ==  "/api/v1/tenpo"
            httpMethod == "POST"
            request == "{aValidRequest}"
        }
        noExceptionThrown()

    }


    def "getById - Api call no encontrado"(){
        given: "Dado un id valido"
        def id = 1L
        repository.getById(id) >> { throw new EntityNotFoundJdbcException(ENTITY_NOT_FOUND_ERROR) }

        when: "Cuando se performe la busqueda y no tenga entidades asociadas"
        useCase.getById(id)

        then: "Entonces debera arrojar exception"
        thrown(EntityNotFoundJdbcException)

    }


}
