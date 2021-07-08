package org.acme.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

//to expose this class to the JAX-RS runtime as a framework enhancement.
@Provider
public class JAXRSExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    final String EXCEPTION_MESSAGE = "invalid request data";
    final String RESPONSE_TYPE = "text/plain";
    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return Response.serverError().entity(EXCEPTION_MESSAGE + ": "
          + exception.getMessage()).type(RESPONSE_TYPE)
          .status(Response.Status.BAD_REQUEST).build();
    }
}
