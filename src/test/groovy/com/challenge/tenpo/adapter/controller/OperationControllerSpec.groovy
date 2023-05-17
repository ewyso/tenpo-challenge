package com.challenge.tenpo.adapter.controller

import com.challenge.tenpo.application.port.in.RegisterApiCallCommand
import com.challenge.tenpo.application.port.in.SumarizeValuesCommand
import com.challenge.tenpo.config.ErrorHandler
import com.challenge.tenpo.domain.NumbersWithPercentage
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class OperationControllerSpec extends Specification {

    SumarizeValuesCommand valuesCommand = Mock(SumarizeValuesCommand)
    RegisterApiCallCommand apiCallCommand = Mock(RegisterApiCallCommand)

    HttpServletRequest httpServletRequest = Mock(HttpServletRequest)

    OperationController controller = new OperationController(valuesCommand, apiCallCommand)


    def mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new ErrorHandler(httpServletRequest))
            .build()

    def "POST - Operate. Dado valores validos, se debera computar el resultado"() {
        given:
        def domain = NumbersWithPercentage.builder().firstNumber(1).secondNumber(5).sumarizedValue(10).build()

        valuesCommand.sumarizeValues(_ as SumarizeValuesCommand.Values) >> domain
        1 * apiCallCommand.registerApiCall(_)

        when:
        def response = mockMvc.perform(
                post("/tenpo/api/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aValidRequest())).andReturn().response

        then:
        with(response){
            status == HttpStatus.OK.value()
            toSingleLine(contentAsString) == """{"sumarized_value":10}"""
        }
    }


    def "POST - Operate. Cuando se produzca un error no debea guardarse el resultado"() {
        given:
        def domain = NumbersWithPercentage.builder().firstNumber(1).secondNumber(5).sumarizedValue(10).build()

        valuesCommand.sumarizeValues(_ as SumarizeValuesCommand.Values) >> { throw new RuntimeException() }
        0 * apiCallCommand.registerApiCall(_)

        when:
        def response = mockMvc.perform(
                post("/tenpo/api/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(aValidRequest())).andReturn().response

        then:
        response.status == HttpStatus.INTERNAL_SERVER_ERROR.value()
    }


    def aValidRequest(){
        """
        {
            "first_value" : 10,
            "second_value" : 13
       }
        """
    }


    def toSingleLine(String value) {
        return value.replaceAll("\\s+", "")
    }

}
