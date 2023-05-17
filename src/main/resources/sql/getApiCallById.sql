SELECT id, endpoint, http_method, request, response, creation_date FROM api_calls_historical
WHERE id = ?