package dev.jramde.saas.exception.response;

import dev.jramde.saas.exception.JrBuisinessException;

public class JrUnauthorizedException extends JrBuisinessException {
    public JrUnauthorizedException(String message) {
        super(message);
    }
}
