package com.challenge.tenpo.adapter.controller;


import com.challenge.tenpo.adapter.controller.model.OperationRestModel;
import com.challenge.tenpo.adapter.controller.model.OperationRestResponse;
import com.challenge.tenpo.application.port.in.RegisterApiCallCommand;
import com.challenge.tenpo.application.port.in.SumarizeValuesCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Validated
@RestController
@RequestMapping("/tenpo/api/v1")
public class OperationController {

    private final SumarizeValuesCommand sumarizeValuesCommand;
    private final RegisterApiCallCommand apiCallCommand;

    public OperationController(SumarizeValuesCommand command, RegisterApiCallCommand apiCallCommand) {
        this.sumarizeValuesCommand = command;
        this.apiCallCommand = apiCallCommand;
    }

    @PostMapping
    public ResponseEntity<OperationRestResponse> operate(@Valid @RequestBody OperationRestModel request,
                                                         HttpServletRequest httpServletRequest
    ) {

        SumarizeValuesCommand.Values values = SumarizeValuesCommand.Values.builder()
                .firstValue(request.getFirstValue())
                .secondValue(request.getSecondValue())
                .build();

        val response = OperationRestResponse.fromDomain(sumarizeValuesCommand.sumarizeValues(values));

        CompletableFuture.runAsync(() ->
                apiCallCommand.registerApiCall(
                        RegisterApiCallCommand.Command.builder()
                                .endpoint(httpServletRequest.getServletPath())
                                .httpMethod(httpServletRequest.getMethod())
                                .request(request)
                                .response(response)
                                .build()
                )
        );

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
