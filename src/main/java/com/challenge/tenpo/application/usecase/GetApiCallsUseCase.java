package com.challenge.tenpo.application.usecase;

import com.challenge.tenpo.application.port.in.GetApiCallsQuery;
import com.challenge.tenpo.application.port.out.ApiCallsRepository;
import com.challenge.tenpo.domain.ApiCallModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GetApiCallsUseCase implements GetApiCallsQuery {

    private final ApiCallsRepository apiCallsRepository;

    public GetApiCallsUseCase(ApiCallsRepository apiCallsRepository) {
        this.apiCallsRepository = apiCallsRepository;
    }

    @Override
    public Page<ApiCallModel> execute(Integer page, Integer size) {
        log.info("Obteniendo los api calls para la pagina {}, y con un offset de {}", page, size);
        Page<ApiCallModel> apiCallPage = apiCallsRepository.getAll(page,size);
        log.info("Pagina de api calls obtenida {}", apiCallPage);
        return apiCallPage;
    }

    @Override
    public ApiCallModel getById(Long id) {
        log.info("Se procede a buscar api call con id: {}", id);
        return apiCallsRepository.getById(id);
    }

}
