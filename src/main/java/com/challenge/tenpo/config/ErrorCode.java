package com.challenge.tenpo.config;

public enum ErrorCode {

    INTERNAL_ERROR(100, "Error interno del servidor"),
    WEB_CLIENT_GENERIC(108, "Error del Web Client"),
    BAD_REQUEST(105, "La request esta mal formateada"),
    UNAUTHORIZED(106, "El token no es valido"),
    JDBC_ERROR(116,"Error con la base de datos"),
    INVALID_PARAMS(137, "Los parametros son incorrectos."),
    CACHE_SAVE_EXCEPTION(100, "Error al registrar el resultado en el sistema"),
    EXTERNAL_SERVICE_ERROR(101, "Error en el servicio de externo"),
    PERCENTAGE_SERVICE_ERROR(123,"Ha ocurrido un error en el servicio externo de porcentajes"),
    ENTITY_NOT_FOUND_ERROR(124,"No se encontro una entidad asociada a los parametros de busqueda"),
    RATE_LIMIT_EXCEED_ERROR(125,"Se supero el maximo de request por minuto");


    private final int value;
    private final String reasonPhrase;

    ErrorCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }
}
