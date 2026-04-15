package dev.jramde.saas.exception;

import lombok.Getter;

@Getter
public class JrBuisinessException extends RuntimeException {
    private final String message;

    public JrBuisinessException(final String message) {
        super(message);
        this.message = message;
    }
}
