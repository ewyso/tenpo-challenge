package com.challenge.tenpo.application.port.out;

import com.challenge.tenpo.domain.ApiCallModel;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ApiCallsRepository {
    void save(String endpoint, String httpMethod, String request, String response);
    Page<ApiCallModel> getAll(Integer page, Integer size);
    ApiCallModel getById(Long id);
}
