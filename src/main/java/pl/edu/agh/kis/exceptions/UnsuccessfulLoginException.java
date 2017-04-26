package pl.edu.agh.kis.exceptions;

import java.util.Map;

public class UnsuccessfulLoginException extends ServerException {

	public UnsuccessfulLoginException(String message) {
		super(message);
	}

	public UnsuccessfulLoginException() {
		super();
	}


}
