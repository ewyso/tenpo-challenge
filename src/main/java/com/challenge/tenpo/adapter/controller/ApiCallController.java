package com.challenge.tenpo.adapter.controller;


import com.challenge.tenpo.adapter.controller.model.ApiCallRestModel;
import com.challenge.tenpo.application.port.in.GetApiCallsQuery;
import com.challenge.tenpo.application.port.in.RegisterApiCallCommand;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Validated
@RestController
@RequestMapping("/tenpo/api/v1/historial")
public class ApiCallController {
    private final GetApiCallsQuery getApiCallsQuery;
    private final RegisterApiCallCommand apiCallCommand;

    public ApiCallController(GetApiCallsQuery getApiCallsQuery, RegisterApiCallCommand apiCallCommand) {
        this.getApiCallsQuery = getApiCallsQuery;
        this.apiCallCommand = apiCallCommand;
    }

    @GetMapping
    public Page<ApiCallRestModel> getAll(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest httpServletRequest
    ) {

        log.info("Obteniendo la lista de api calls para la pagina: {} y cantidad por pagina: {}", page, size);
        Page<ApiCallRestModel> apiCallPageRest = getApiCallsQuery.execute(page, size).map(ApiCallRestModel::fromDomain);

        CompletableFuture.runAsync(() ->
                registerApiCall(
                        httpServletRequest.getServletPath(),
                        httpServletRequest.getMethod(),
                        httpServletRequest.getParameterMap(),
                        apiCallPageRest
                )
        );

        log.info("Respuesta del servicio {}", apiCallPageRest);
        return apiCallPageRest;
    }


    @GetMapping("/{id}")
    public ApiCallRestModel getById(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        log.info("Obteniendo la lista de api calls para id: {}", id);
        val response = ApiCallRestModel.fromDomain(getApiCallsQuery.getById(id));

        CompletableFuture.runAsync(() ->
                registerApiCall(
                        httpServletRequest.getServletPath(),
                        httpServletRequest.getMethod(),
                        httpServletRequest.getParameterMap(),
                        response
                )
        );
        log.info("Respuesta del servicio de busqueda: {}", response);
        return response;
    }

    private void registerApiCall(String endpoint, String httpMethod, Object request, Object response) {
        apiCallCommand.registerApiCall(
                RegisterApiCallCommand.Command.builder()
                        .endpoint(endpoint)
                        .httpMethod(httpMethod)
                        .request(request)
                        .response(response)
                        .build()
        );
    }

}
