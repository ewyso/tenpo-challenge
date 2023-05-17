package com.challenge.tenpo.application.usecase;

import com.challenge.tenpo.application.port.in.RegisterApiCallCommand;
import com.challenge.tenpo.application.port.out.ApiCallsRepository;
import com.challenge.tenpo.commons.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RegisterApiCallUseCase implements RegisterApiCallCommand {
    private final ApiCallsRepository apiCallsRepository;
    private final JsonMapper jsonMapper;

    public RegisterApiCallUseCase(ApiCallsRepository apiCallsRepository, JsonMapper jsonMapper) {
        this.apiCallsRepository = apiCallsRepository;

        this.jsonMapper = jsonMapper;
    }

    @Override
    public void registerApiCall(Command command) {
        try {
            apiCallsRepository.save(
                    command.getEndpoint(),
                    command.getHttpMethod(),
                    jsonMapper.serialize(command.getRequest()),
                    jsonMapper.serialize(command.getResponse())
            );
        } catch (Exception e){
            log.error("Ocurrio un error al salvar API Call para endpoint: {}", command.getEndpoint());
        }

    }
}
