package com.ratelimiter.proxy;

public interface ApiService {
    /**
     * Executes the functional API request.
     * @return Response string or status code.
     */
    String callApi(String endpoint);
}
