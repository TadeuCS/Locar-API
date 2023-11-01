package br.com.locar.api.exceptions;

public class NotFoundRegitreException extends RuntimeException{
    public NotFoundRegitreException(String message) {
        super(message);
    }
}
