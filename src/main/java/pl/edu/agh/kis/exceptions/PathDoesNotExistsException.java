package pl.edu.agh.kis.exceptions;

import java.util.Map;

public class PathDoesNotExistsException extends ServerException {

	public PathDoesNotExistsException(String message) {
		super(message);
	}

	public PathDoesNotExistsException() {
		super();
	}


}
