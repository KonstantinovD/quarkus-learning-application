package org.acme.beans.jaxrsinterceptors;

import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.*;
import java.io.IOException;
import java.io.OutputStream;

@Provider
@Slf4j
public class ReaderWriterInterceptor
  implements ReaderInterceptor, WriterInterceptor {

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
      throws IOException, WebApplicationException {
      final OutputStream outputStream = context.getOutputStream();
      String encrypted = "encrypted |";
      outputStream.write(encrypted.getBytes());
      context.setOutputStream(outputStream);
      context.proceed();
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context)
      throws IOException, WebApplicationException {
        log.info(String.valueOf(context.getInputStream().available()));
        // put the inbound data back where we found it.
        // (See LoggingFilter class)
        return context.proceed();
    }
}
