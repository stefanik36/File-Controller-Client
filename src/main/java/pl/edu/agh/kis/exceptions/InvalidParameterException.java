package pl.edu.agh.kis.exceptions;

import java.util.Map;

public class InvalidParameterException extends ServerException {

	public InvalidParameterException(String message) {
		super(message);
	}

	public InvalidParameterException() {
		super();
	}


}
