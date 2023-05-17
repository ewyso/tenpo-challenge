package com.challenge.tenpo.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCallModel {
    String endpoint;
    String httpMethod;
    String request;
    String response;
}
