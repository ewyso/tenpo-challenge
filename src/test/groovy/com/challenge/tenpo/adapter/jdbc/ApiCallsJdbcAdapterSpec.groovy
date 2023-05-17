package com.challenge.tenpo.adapter.jdbc

import com.challenge.tenpo.adapter.jdbc.exception.EntityNotFoundJdbcException
import com.challenge.tenpo.adapter.jdbc.model.ApiCallJdbcModel
import com.challenge.tenpo.adapter.jdbc.model.ApiCallJdbcModelMapper
import com.challenge.tenpo.application.exception.AdapterException
import org.springframework.dao.PermissionDeniedDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import spock.lang.Specification

class ApiCallsJdbcAdapterSpec extends Specification {

    JdbcTemplate jdbcTemplate = Mock(JdbcTemplate)

    ApiCallsJdbcAdapter adapter = new ApiCallsJdbcAdapter(jdbcTemplate)

    def "save - Dado parametros de entrada validos, debera guardar en db el registro"(){
        given:
        def endpoint = "/api/v1/tenpo"
        def method = "POST"
        def request = "aValidRequest"
        def response = "aValidResponse"

        1 * jdbcTemplate.update(_ as String, endpoint, method, request, response) >> {}

        when:
        adapter.save(endpoint, method, request, response)

        then:
        noExceptionThrown()
    }

    def "save - Dado parametros de entrada validos, cuando ocurra un error, debera manejarlo domo AdapterException"(){
        given:
        1 * jdbcTemplate.update(_ as String, _, _, _, _) >> { throw new PermissionDeniedDataAccessException("error", new RuntimeException())}

        when:
        adapter.save("api/v1/tenpo", "POST", "request", "response")

        then:
        thrown(AdapterException)
    }


    def "getById - Dado un id valido, debera retornar un apiCallModel asociado al mismo"(){
        given:
        def aValidJdbcModel = ApiCallJdbcModel.builder().request("request").response("response").build()
        1 * jdbcTemplate.query(_ as String, _, _) >> { [aValidJdbcModel] }

        when:
        def result = adapter.getById(1L)

        then:
        result.request == "request"
        noExceptionThrown()
    }

    def "getById - Dado un id valido, cuando no haya registro, debera arrojar EntityNotFoundJdbcException"(){
        given:
        1 * jdbcTemplate.query(_ as String, _, _) >> { [] }

        when:
        adapter.getById(1L)

        then:
        thrown(EntityNotFoundJdbcException)
    }


    def "getAll - Dado parametros valido de busqueda, debera devolver contenido paginado con el size indicado"(){
        given:
        def aValidJdbcModel = ApiCallJdbcModel.builder().request("request").response("response").build()
        def page = 1
        def size = 2

        jdbcTemplate.queryForObject(_, _) >> 10
        jdbcTemplate.query(_, _ as ApiCallJdbcModelMapper) >> { [aValidJdbcModel, aValidJdbcModel] }


        when:
        def result = adapter.getAll(page, size)

        then:
        result.size == 2
        noExceptionThrown()

    }




}
