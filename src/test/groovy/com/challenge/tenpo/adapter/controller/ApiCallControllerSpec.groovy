package com.challenge.tenpo.adapter.controller

import com.challenge.tenpo.adapter.jdbc.exception.EntityNotFoundJdbcException
import com.challenge.tenpo.application.port.in.GetApiCallsQuery
import com.challenge.tenpo.application.port.in.RegisterApiCallCommand
import com.challenge.tenpo.config.ErrorCode
import com.challenge.tenpo.config.ErrorHandler
import com.challenge.tenpo.domain.ApiCallModel
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

class ApiCallControllerSpec extends Specification {

    GetApiCallsQuery query = Mock(GetApiCallsQuery)
    RegisterApiCallCommand command = Mock(RegisterApiCallCommand)

    HttpServletRequest httpServletRequest = Mock(HttpServletRequest)

    ApiCallController controller = new ApiCallController(query, command)

    def mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new ErrorHandler(httpServletRequest))
            .build()


    def "getById - Dado un id valido, debera devolver el registro asociado"(){
        given:
        def id = 1L
        def domain = ApiCallModel.builder().request("request").response("response").build()
        query.getById(id) >> domain

        1 * command.registerApiCall(_)

        when:
        def response = mockMvc.perform(get("/tenpo/api/v1/historial/${id}"))
                .andReturn().response

        then:
        response.status == HttpStatus.OK.value()
        toSingleLine(response.contentAsString) == """{"request":"request","response":"response"}"""
    }


    def "getById - Dado un id valido, que no tenga registros asociados, no debera deolver nada y no guardara registro"(){
        given:
        def id = 1L
        query.getById(id) >> { throw new EntityNotFoundJdbcException(ErrorCode.ENTITY_NOT_FOUND_ERROR) }

        0 * command.registerApiCall(_)

        when:
        def response = mockMvc.perform(get("/tenpo/api/v1/historial/${id}"))
                .andReturn().response

        then:
        response.status == HttpStatus.NOT_FOUND.value()
    }


    def toSingleLine(String value) {
        return value.replaceAll("\\s+", "")
    }


}
