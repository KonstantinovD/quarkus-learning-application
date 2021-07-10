package org.acme.beans.exceptionmapper;

public class CustomException extends RuntimeException{
    public CustomException(String cause) {
        super(cause);
    }
}
