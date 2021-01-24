package com.smallbazaar.rest.exceptions;

public class UserAlreadyExistException extends Exception {
	private static final long serialVersionUID = 1L;

	public UserAlreadyExistException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

	public UserAlreadyExistException(String exMessage) {
        super(exMessage);
    }
}
