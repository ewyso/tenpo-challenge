package com.challenge.tenpo.commons;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Slf4j
@Service
public class JsonMapper {

    public final ObjectMapper mapper;

    public JsonMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @SneakyThrows
    public String serialize(final Object input) {
        return mapper.writeValueAsString(input);
    }
    @SneakyThrows
    public <T> T deserialize(final String input, final Class<T> clazz) {
        return mapper.readValue(input, clazz);
    }

    @SneakyThrows
    public <T> T deserialize(final String input, final Type type) {
        return mapper.readValue(input, mapper.constructType(type));
    }

    @SneakyThrows
    public <T> T deserialize(final String input, final TypeReference<T> typeReference) {
        return mapper.readValue(input, typeReference);
    }
}
