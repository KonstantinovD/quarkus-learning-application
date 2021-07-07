package org.acme.beans.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.acme.beans.qualifiers.Audited;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;

@Audited
@Interceptor // marks this class as an interceptor
@Priority(Interceptor.Priority.APPLICATION) // numerical rank, relative to other interceptors
@Slf4j
public class AuditingInterceptor implements Serializable {

    // annotation will cause this method to be invoked whenever a
    // method with my annotation '@Audited' is invoked
    // An interceptor class can have any number of @AroundInvoke methods
    // they will all be executed in order of appearance in the class.
    @AroundInvoke
    public Object logEExecution(
    //InvocationContext gives information about the intercepted method
      InvocationContext invocationContext) throws Exception {
        log.info("Method: " + invocationContext.getMethod().getName());
        log.info("Arguments: " + invocationContext.getParameters());
        log.info("Executing the called method");
        // MUST call proceed on the InvocationContext, to ensure
        // that the intercepted method gets invoked
        Object result = invocationContext.proceed();
        log.info("The object the method was invoked on: "
          + invocationContext.getTarget());
        return result;
    }
}
