package io.agileintelligence.ppmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNameAlreadyExists extends RuntimeException {
    public UserNameAlreadyExists(String message) {
        super(message);
    }
}
