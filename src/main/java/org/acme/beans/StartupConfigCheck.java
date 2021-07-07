package org.acme.beans;

import io.quarkus.runtime.Startup;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.util.logging.Logger;

// All the bean initializations are lazy by default
@ApplicationScoped
// You can still force eager initialization with the
@Startup
public class StartupConfigCheck {

    Logger logger = Logger.getLogger(StartupConfigCheck.class.getName());

    @PostConstruct
    public void startupOperation() {
        logger.info("StartupConfigCheck post construct execution");
    }

    @PreDestroy
    public void shutdownOperation() {
        logger.info("StartupConfigCheck pre destroy execution");
    }

    public String getMessage() {
        logger.info("StartupConfigCheck normal execution");
        return "StartupConfigCheck message";
    }
}
