package org.acme.beans.jaxrsinterceptors;

import lombok.extern.slf4j.Slf4j;
import org.acme.beans.jaxrsfilters.Address;

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
        // it will be executed for all contexts with type Address
        if (context.getEntity() instanceof Address) {
            final OutputStream outputStream = context.getOutputStream();
            String encrypted = "encrypted |";
            outputStream.write(encrypted.getBytes());
            context.setOutputStream(outputStream);

        }
        // but in any case we should proceed received context
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
