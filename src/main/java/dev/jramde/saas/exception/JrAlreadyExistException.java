package dev.jramde.saas.exception;

import lombok.Getter;

@Getter
public class JrAlreadyExistException extends JrBuisinessException {

    public JrAlreadyExistException(String message) {
        super(message);
    }
}
