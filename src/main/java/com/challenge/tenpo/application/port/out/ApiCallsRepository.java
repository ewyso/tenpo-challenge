package com.challenge.tenpo.application.port.out;

import com.challenge.tenpo.domain.ApiCallModel;
import org.springframework.data.domain.Page;

public interface ApiCallsRepository {
    void save(String endpoint, String httpMethod, String request, String response);
    Page<ApiCallModel> getAll(Integer page, Integer size);
    ApiCallModel getById(Long id);
}
