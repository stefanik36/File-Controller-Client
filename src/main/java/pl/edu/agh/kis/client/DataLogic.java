package pl.edu.agh.kis.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import pl.edu.agh.kis.exceptions.AlreadyExistsException;
import pl.edu.agh.kis.exceptions.AuthorizationRequiredException;
import pl.edu.agh.kis.exceptions.InvalidParameterException;
import pl.edu.agh.kis.exceptions.PathDoesNotExistsException;
import pl.edu.agh.kis.exceptions.ServerException;
import pl.edu.agh.kis.exceptions.UnknownCodeException;
import pl.edu.agh.kis.exceptions.UnsuccessfulLoginException;
import pl.edu.agh.kis.model.Data;

public abstract class DataLogic {

	final int SUCCESSFUL = 200;
	final int CREATED = 201;
	final int DELETED = 204;

	final int ALREADY_EXISTS = 400;
	final int AUTHORIZATION_REQUIRED = 401;
	final int UNSUCCESSFUL_LOGGIN = 403;
	final int PATH_DOES_NOT_EXISTS = 404;
	final int INVALID_PARAMETER = 405;

	final String server = "https://localhost:4567";
	final String files = "/files/";
	
	final Gson gson = new Gson();
	
	protected String sessionId;

//	protected DataLogic() {
//		this(null);
//	}
//
//	public DataLogic(String sessionId) {
//		this.sessionId = sessionId;
//	}

	abstract Data getMetadata(Path path) throws ServerException, UnirestException, UnsupportedEncodingException;

	abstract Data rename(Path path, String newName);

	abstract Data move(Path oldPath, Path newPath);

	abstract Data delete(Path path);

	void checkCode(int code) throws ServerException {
		if ((code == SUCCESSFUL) || (code == CREATED) || (code == DELETED)) {
			return;
		} else if (code == ALREADY_EXISTS) {
			throw new AlreadyExistsException();
		} else if (code == AUTHORIZATION_REQUIRED) {
			throw new AuthorizationRequiredException();
		} else if (code == UNSUCCESSFUL_LOGGIN) {
			throw new UnsuccessfulLoginException();
		} else if (code == PATH_DOES_NOT_EXISTS) {
			throw new PathDoesNotExistsException();
		} else if (code == INVALID_PARAMETER) {
			throw new InvalidParameterException();
		} else {
			throw new UnknownCodeException("Unknown code: " + code);
		}
	}
	HttpResponse<String> getMetadataResponse(Path path)
			throws UnsupportedEncodingException, UnirestException {
		System.out.println("path string: "+path.toString());
		String url = server + files + encodeToURL(path.toString()) + "/get_meta_data";
		HttpResponse<String> jsonResponse = Unirest.get(url).header("cookie", changeSessionIdToSendByCookie(sessionId))
				.asString();
		return jsonResponse;
	}
	
	private String encodeToURL(String str) throws UnsupportedEncodingException {
		return URLEncoder.encode(str, "UTF-8");
	}

	private String changeSessionIdToSendByCookie(String sessionId) {
		return "sessionid=" + sessionId;
	}
}
