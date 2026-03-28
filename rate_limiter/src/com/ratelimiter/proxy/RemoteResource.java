package com.ratelimiter.proxy;

public interface RemoteResource {
    /**
     * Executes the functional logic of the remote resource.
     * @param input Data component for the API call.
     * @return Response string from the remote server.
     */
    String callApi(String input);
}
