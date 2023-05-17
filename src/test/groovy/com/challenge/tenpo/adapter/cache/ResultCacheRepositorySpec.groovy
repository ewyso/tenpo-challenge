package com.challenge.tenpo.adapter.cache

import com.challenge.tenpo.adapter.cache.exception.CacheSaveException
import com.challenge.tenpo.adapter.cache.model.ResultCacheModel
import com.challenge.tenpo.config.ErrorCode
import com.challenge.tenpo.domain.NumbersWithPercentage
import com.google.common.cache.Cache
import spock.lang.Specification

class ResultCacheRepositorySpec extends Specification {


    Cache<String, ResultCacheModel> cache = Mock(Cache<String, ResultCacheModel>)

    ResultCacheRepository repository = new ResultCacheRepository(cache)

    def "Save - Dado una key y un modelo valido, debera guardarlo en cache"() {
        given:
        def key = "10:10"
        def model = aValidModel()

        cache.put(key, _) >> {}

        when:
        repository.save(key, model)

        then:
        noExceptionThrown()

    }

    def "Save - Cuando se produzca un error al guardar debera arrojar CacheSaveException"() {
        given:
        def key = "10:10"
        def model = aValidModel()

        cache.put(key, _) >> { throw new CacheSaveException(ErrorCode.CACHE_SAVE_EXCEPTION) }

        when:
        repository.save(key, model)

        then:
        thrown(CacheSaveException)

    }

    def "Get - Cuando el object se encuentre en cache debera devolverlo"() {
        given:
        def key = "10:10"
        def resultCacheModel = ResultCacheModel.builder().firstValue(10L).secondeValue(15L).result(123L).build()

        cache.getIfPresent(key) >> resultCacheModel

        when:
        def result = repository.get(key)

        then:
        result.sumarizedValue == 123L

    }

    def "Get - Cuando el object no se encuentre en cache debera devolver null"() {
        given:
        def key = "10:10"

        cache.getIfPresent(key) >> null

        when:
        def result = repository.get(key)

        then:
        result == null

    }

    static aValidModel() {
        return NumbersWithPercentage.builder()
                .firstNumber(10)
                .secondNumber(10)
                .sumarizedValue(500)
                .build()
    }
}
