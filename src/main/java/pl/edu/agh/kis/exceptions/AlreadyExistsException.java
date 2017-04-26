package pl.edu.agh.kis.exceptions;

import java.util.Map;

public class AlreadyExistsException extends ServerException {

	public AlreadyExistsException(String message) {
		super(message);
	}

	public AlreadyExistsException() {
		super();
	}

}
