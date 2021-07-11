package org.acme.beans.healthcheck;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Liveness
public class HealthCheckProvider implements HealthCheck {
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.up("I'm alive");
    }
}

