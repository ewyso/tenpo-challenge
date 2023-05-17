package com.challenge.tenpo.application.port.in;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

public interface RegisterApiCallCommand {

    void registerApiCall(Command command);

    @Value
    @Builder
    @ToString
    class Command {
        String endpoint;
        String httpMethod;
        Object request;
        Object response;
    }

}
