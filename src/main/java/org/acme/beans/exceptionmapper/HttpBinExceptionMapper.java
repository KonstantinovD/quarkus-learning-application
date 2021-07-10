package org.acme.beans.exceptionmapper;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import javax.ws.rs.core.Response;

public class HttpBinExceptionMapper implements ResponseExceptionMapper<CustomException> {
    @Override
    public CustomException toThrowable(Response response) {
        // do something with the content of the response and
        // rethrow the exception
        String errorMessage = response.readEntity(String.class);
        errorMessage = "RETHROW EXCEPTION" + errorMessage;
        return new CustomException(errorMessage);
    }
}
