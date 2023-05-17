package com.challenge.tenpo.testData

import com.challenge.tenpo.domain.ApiCallModel

class ApiCallModelTestData {

    def static aValidApiCallModel(){
        return new ApiCallModel(
                "/api/v1/tenpo",
                "POST",
                "{aValidRequest}",
                "{aValidResponse}"
        )
    }
}
