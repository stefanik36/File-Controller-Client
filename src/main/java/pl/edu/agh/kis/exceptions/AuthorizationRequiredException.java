package pl.edu.agh.kis.exceptions;

import java.util.Map;

public class AuthorizationRequiredException extends ServerException {

	public AuthorizationRequiredException(String message) {
		super(message);
	}

	public AuthorizationRequiredException() {
		super();
	}


}
