package com.challenge.tenpo.application.port.in;

import com.challenge.tenpo.domain.ApiCallModel;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface GetApiCallsQuery {
    Page<ApiCallModel> execute(Integer page, Integer size);
    ApiCallModel getById(Long id);
}
