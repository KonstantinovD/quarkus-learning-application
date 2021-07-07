package org.acme.beans.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.acme.beans.qualifiers.Audited;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Slf4j
public class Generator {
    @Audited
    public void generate(String source){
        log.info("start Generator method");
        log.info("source: " + source);
        log.info("end Generator method");
    }
}
