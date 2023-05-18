CREATE TABLE IF NOT EXISTS api_calls_historical (
    id SERIAL PRIMARY KEY,
    endpoint VARCHAR(50),
    http_method VARCHAR(5),
    request VARCHAR(50),
    response VARCHAR,
    creation_date timestamp NOT NULL DEFAULT NOW()
);