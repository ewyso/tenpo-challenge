package com.challenge.tenpo.adapter.cache;

import com.challenge.tenpo.adapter.cache.exception.CacheSaveException;
import com.challenge.tenpo.adapter.cache.model.ResultCacheModel;
import com.challenge.tenpo.application.port.out.ResultsRepository;
import com.challenge.tenpo.domain.NumbersWithPercentage;
import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.challenge.tenpo.config.ErrorCode.CACHE_SAVE_EXCEPTION;

@Slf4j
@Component
public class ResultCacheRepository implements ResultsRepository {

    private @Qualifier("resultCache") Cache<String, ResultCacheModel> cache;


    public ResultCacheRepository(Cache<String, ResultCacheModel> cache) {
        this.cache = cache;
    }

    @Override
    public void save(String key, NumbersWithPercentage numbersWithPercentage) {
        try {
            val cacheable = buildCacheableResult(numbersWithPercentage);
            log.info("Guardando en cache el objeto de resultado: {}", cacheable);
            cache.put(key, cacheable);
        } catch (Exception e){
            log.error("Error al guardar resultado en cache", e);
            throw new CacheSaveException(CACHE_SAVE_EXCEPTION);
        }
    }

    @Override
    public NumbersWithPercentage get(String key) {
        log.info("Se busca en cache el resultado para la key: {}", key);
        val result = cache.getIfPresent(key);
        return result != null ? ResultCacheModel.toDomain(result) : null;
    }

    private ResultCacheModel buildCacheableResult(NumbersWithPercentage domain){
            return ResultCacheModel.builder()
                    .firstValue(domain.getFirstNumber())
                    .secondeValue(domain.getSecondNumber())
                    .result(domain.getSumarizedValue())
                    .build();
    }
}
