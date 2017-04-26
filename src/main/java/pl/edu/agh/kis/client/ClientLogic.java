package pl.edu.agh.kis.client;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import pl.edu.agh.kis.exceptions.AlreadyExistsException;
import pl.edu.agh.kis.exceptions.AuthorizationRequiredException;
import pl.edu.agh.kis.exceptions.InvalidLoginOrPassword;
import pl.edu.agh.kis.exceptions.InvalidParameterException;
import pl.edu.agh.kis.exceptions.NotPermittedCharactersException;
import pl.edu.agh.kis.exceptions.PathDoesNotExistsException;
import pl.edu.agh.kis.exceptions.UnknownCodeException;
import pl.edu.agh.kis.exceptions.UnsuccessfulLoginException;
import pl.edu.agh.kis.model.File;
import pl.edu.agh.kis.model.Folder;
import pl.edu.agh.kis.model.User;

public class ClientLogic {

	// codes:
	private final int SUCCESSFUL = 200;
	private final int CREATED = 201;
	private final int DELETED = 204;

	private final int ALREADY_EXISTS = 400;
	private final int AUTHORIZATION_REQUIRED = 401;
	private final int UNSUCCESSFUL_LOGGIN = 403;
	private final int PATH_DOES_NOT_EXISTS = 404;
	private final int INVALID_PARAMETER = 405;

	private final Gson gson = new Gson();


	// STOP //TODO DELETE //
	public void stopServer() {
		try {
			Unirest.get("https://localhost:4567/stop").asJson();
		} catch (UnirestException e) {
			// e.printStackTrace();
		}
	}
	// END//

	// GET METADATA//
	public Folder getFolderMetadata(String path, String sessionId) throws UnirestException, InvalidParameterException,
			AuthorizationRequiredException, UnknownCodeException, PathDoesNotExistsException {

		String finalPath = changeFolderPathToSend(path);

		String url = "https://localhost:4567/files/" + finalPath + "/get_meta_data";
		String finalSessionId = changeSessionIdToSend(sessionId);
		HttpResponse<String> jsonResponse = Unirest.get(url).header("cookie", finalSessionId).asString();
		int code = jsonResponse.getCode();
		if (code == SUCCESSFUL) {
			Folder f = gson.fromJson(jsonResponse.getBody(), Folder.class);
			return f;
		} else if (code == AUTHORIZATION_REQUIRED) {
			throw new AuthorizationRequiredException("Not your folder.");
		} else if (code == INVALID_PARAMETER) {
			throw new InvalidParameterException("Path parameter is invalid: " + path);
		} else if (code == PATH_DOES_NOT_EXISTS) {
			throw new PathDoesNotExistsException("Path do not exists: " + path);
		} else {
			throw new UnknownCodeException("Unknown code: " + code);
		}

	}

	private String changeSessionIdToSend(String sessionId) {
		String finalSessionId = "sessionid=" + sessionId;
		return finalSessionId;
	}
	// TODO FILE GET METADATA
	// END//

	// LIST FOLDER CONTENT//
	public List<Object> listFolderContent(String path, boolean recursive, String sessionId)
			throws InvalidParameterException, UnirestException, AuthorizationRequiredException,
			PathDoesNotExistsException, UnknownCodeException {
		String finalPath = changeFolderPathToSend(path);
		String url = "https://localhost:4567/files/" + finalPath + "/list_folder_content?recursive=" + recursive;
		String finalSessionId = changeSessionIdToSend(sessionId);
		HttpResponse<String> jsonResponse = Unirest.get(url).header("cookie", finalSessionId).asString();
		int code = jsonResponse.getCode();
		if (code == SUCCESSFUL) {
			List<Object> folderAndFilesList = new ArrayList<Object>();
			folderAndFilesList = gson.fromJson(jsonResponse.getBody(), folderAndFilesList.getClass());
			return folderAndFilesList;
		} else if (code == AUTHORIZATION_REQUIRED) {
			throw new AuthorizationRequiredException("Not your folder.");
		} else if (code == INVALID_PARAMETER) {
			throw new InvalidParameterException("Path parameter is invalid: " + path);
		} else if (code == PATH_DOES_NOT_EXISTS) {
			throw new PathDoesNotExistsException("Path do not exists: " + path);
		} else {
			throw new UnknownCodeException("Unknown code: " + code);
		}
	}
	// END//

	// CREATE FOLDER//
	public Folder createFolder(String newFolderPath, String sessionId)
			throws InvalidParameterException, UnirestException, AuthorizationRequiredException,
			PathDoesNotExistsException, UnknownCodeException, AlreadyExistsException {
		String finalPath = changeFolderPathToSend(newFolderPath);

		String url = "https://localhost:4567/files/" + finalPath + "/create_directory";
		String finalSessionId = changeSessionIdToSend(sessionId);
		HttpResponse<String> jsonResponse = Unirest.put(url).header("cookie", finalSessionId).asString();
		int code = jsonResponse.getCode();
		if (code == CREATED) {
			Folder f = gson.fromJson(jsonResponse.getBody(), Folder.class);
			return f;
		} else if (code == AUTHORIZATION_REQUIRED) {
			throw new AuthorizationRequiredException("Authorization required.");
		} else if (code == INVALID_PARAMETER) {
			throw new InvalidParameterException("Path parameter is invalid: " + newFolderPath);
		} else if (code == ALREADY_EXISTS) {
			throw new AlreadyExistsException("Folder with path: " + newFolderPath + " already exists.");
		} else if (code == PATH_DOES_NOT_EXISTS) {
			String parentPath = getParentPath(newFolderPath);
			throw new PathDoesNotExistsException("Path does not exists: " + parentPath);
		} else {
			throw new UnknownCodeException("Unknown code: " + code);
		}
	}
	// END//

	private String getParentPath(String path) throws InvalidParameterException {
		String[] parts = path.split("/");
		StringBuilder parentPathBuilder = new StringBuilder("");

		for (int i = 1; i < parts.length - 1; i++) {
			parentPathBuilder.append("/");
			parentPathBuilder.append(parts[i]);
		}
		parentPathBuilder.append("/");
		String parentFolderPath = parentPathBuilder.toString();

		String pathPattern = "(/[a-zA-Z0-9_-]+)+/";
		Pattern p = Pattern.compile(pathPattern);
		Matcher m = p.matcher(parentFolderPath);

		boolean isMatch = m.matches();
		if (!isMatch) {
			System.out.println("-----------------fail----------------getParentFolderPath: " + path);
			throw new InvalidParameterException("Mismatched pattern in path.");
		}
		return parentFolderPath;
	}

	// DELETE FOLDER//
	public boolean deleteFolder(String path, String sessionId) throws InvalidParameterException, UnirestException,
			AuthorizationRequiredException, PathDoesNotExistsException, UnknownCodeException {

		String finalPath = changeFolderPathToSend(path);
		String url = "https://localhost:4567/files/" + finalPath + "/delete";
		String finalSessionId = changeSessionIdToSend(sessionId);
		HttpResponse<String> jsonResponse = Unirest.delete(url).header("cookie", finalSessionId).asString();
		int code = jsonResponse.getCode();
		if (code == DELETED) {
			return true;
		} else if (code == AUTHORIZATION_REQUIRED) {
			throw new AuthorizationRequiredException("Not your folder or file.");
		} else if (code == INVALID_PARAMETER) {
			throw new InvalidParameterException("Path parameter is invalid: " + path);
		} else if (code == PATH_DOES_NOT_EXISTS) {
			throw new PathDoesNotExistsException("Path do not exists: " + path);
		} else {
			throw new UnknownCodeException("Unknown code: " + code);
		}
	}
	// END//

	// RENAME FOLDER//
	public Folder renameFolder(String folderPath, String newName, String sessionId)
			throws InvalidParameterException, UnirestException, AuthorizationRequiredException,
			PathDoesNotExistsException, UnknownCodeException, AlreadyExistsException {
		String finalPath = changeFolderPathToSend(folderPath);
		String finalNewName = changeFolderNameToSend(newName);
		String url = "https://localhost:4567/files/" + finalPath + "/rename?new_name=" + finalNewName;
		String finalSessionId = changeSessionIdToSend(sessionId);
		HttpResponse<String> jsonResponse = Unirest.put(url).header("cookie", finalSessionId).asString();
		int code = jsonResponse.getCode();
		if (code == SUCCESSFUL) {
			Folder f = gson.fromJson(jsonResponse.getBody(), Folder.class);
			return f;
		} else if (code == AUTHORIZATION_REQUIRED) {
			throw new AuthorizationRequiredException("Parent folder is not your folder.");
		} else if (code == INVALID_PARAMETER) {
			throw new InvalidParameterException("Path parameter is invalid: " + folderPath);
		} else if (code == ALREADY_EXISTS) {
			throw new AlreadyExistsException("Folder with name: " + newName + " already exists.");
		} else if (code == PATH_DOES_NOT_EXISTS) {
			throw new PathDoesNotExistsException("Path do not exists: " + folderPath);
		} else {
			throw new UnknownCodeException("Unknown code: " + code);
		}
	}
	// END//

	// MOVE FOLDER//
	public Folder moveFolder(String oldPath, String newPath, String sessionId)
			throws InvalidParameterException, UnirestException, AuthorizationRequiredException,
			PathDoesNotExistsException, UnknownCodeException, AlreadyExistsException {

		String finalOldPath = changeFolderPathToSend(oldPath);
		String finalNewPath = changeFolderPathToSend(newPath);
		String url = "https://localhost:4567/files/" + finalOldPath + "/move?new_path=" + finalNewPath;
		String finalSessionId = changeSessionIdToSend(sessionId);
		HttpResponse<String> jsonResponse = Unirest.put(url).header("cookie", finalSessionId).asString();
		int code = jsonResponse.getCode();
		if (code == SUCCESSFUL) {
			Folder f = gson.fromJson(jsonResponse.getBody(), Folder.class);
			return f;
		} else if (code == AUTHORIZATION_REQUIRED) {
			throw new AuthorizationRequiredException("This is not your folder.");
		} else if (code == INVALID_PARAMETER) {
			throw new InvalidParameterException("Path parameter is invalid: " + oldPath + " or: " + newPath);
		} else if (code == ALREADY_EXISTS) {
			throw new AlreadyExistsException("Folder: " + newPath + " already exists.");
		} else if (code == PATH_DOES_NOT_EXISTS) {
			throw new PathDoesNotExistsException("Path do not exists: " + oldPath + " or: " + newPath);
		} else {
			throw new UnknownCodeException("Unknown code: " + code);
		}
	}
	// END//
	
	// UPLOAD FILE//
	public File uploadFile(String content, String path, String sessionId)
			throws InvalidParameterException, UnirestException, AuthorizationRequiredException, AlreadyExistsException,
			PathDoesNotExistsException, UnknownCodeException{
		String finalPath = changeFolderPathToSend(path);
		String finalSessionId = changeSessionIdToSend(sessionId);
		String url = "https://localhost:4567/files/upload?path=" + finalPath;
		HttpResponse<String> jsonResponse = Unirest.put(url).header("cookie", finalSessionId).body(content).asString();
		int code = jsonResponse.getCode();
		if (code == CREATED) {
			File f = gson.fromJson(jsonResponse.getBody(), File.class);
			return f;
		} else if (code == AUTHORIZATION_REQUIRED) {
			throw new AuthorizationRequiredException("This is not your folder.");
		} else if (code == INVALID_PARAMETER) {
			throw new InvalidParameterException("Path parameter is invalid: "+path);
		} else if (code == ALREADY_EXISTS) {
			throw new AlreadyExistsException("File: " + path + " already exists.");
		} else if (code == PATH_DOES_NOT_EXISTS) {
			throw new PathDoesNotExistsException("Path do not exists: " + path);
		} else {
			throw new UnknownCodeException("Unknown code: " + code);
		}
	}
	//END//

	// CREATE USER//
	public User createUser(String name, String pass)
			throws UnknownCodeException, UnirestException, AlreadyExistsException, NotPermittedCharactersException {

		HttpResponse<JsonNode> jsonResponse = Unirest.post("https://localhost:4567/users/create_user")
				.field("user_name", name).field("user_pass", pass).asJson();

		int code = jsonResponse.getCode();
		if (code == CREATED) {
			User u = gson.fromJson(jsonResponse.getBody().toString(), User.class);
			return u;
		} else if (code == ALREADY_EXISTS) {
			throw new AlreadyExistsException("User with name: " + name + " already exists.");
		} else if (code == INVALID_PARAMETER) {
			throw new NotPermittedCharactersException("Login or password have not permitted characters.");
		} else {
			throw new UnknownCodeException("Unknown code: " + code);
		}

	}
	// END//

	// LOG IN//
	public String logIn(String name, String pass) throws UnsuccessfulLoginException, UnirestException,
			InvalidParameterException, UnknownCodeException, InvalidLoginOrPassword {

		String authorization = name + ":" + pass;

		byte[] encodedBytes = Base64.encodeBase64(authorization.getBytes());
		String encodedAuthorization = "Basic " + new String(encodedBytes);

		HttpResponse<String> jsonResponse = Unirest.get("https://localhost:4567/users/access")
				.header("authorization", encodedAuthorization).asString();
		int code = jsonResponse.getCode();
		if (code == SUCCESSFUL) {
			return loggedInResult(jsonResponse.getHeaders().get("set-cookie"));
		} else if (code == UNSUCCESSFUL_LOGGIN) {
			throw new UnsuccessfulLoginException("Wrong login or password.");
		} else if (code == INVALID_PARAMETER) {
			throw new InvalidLoginOrPassword("Invalid login or password.");
		} else {
			throw new UnknownCodeException("Unknown code: " + code);
		}

	}
	// END//

	private String changeFolderPathToSend(String path) throws InvalidParameterException {
		checkFolderPath(path);
		String[] pathParts = path.split("/");
		StringBuilder pathBuilder = new StringBuilder("");

		for (int i = 1; i < pathParts.length; i++) {
			pathBuilder.append("%2F");
			pathBuilder.append(pathParts[i]);
		}
		pathBuilder.append("%2F");
		String finalPath = pathBuilder.toString();
		return finalPath;
	}

	private void checkFolderPath(String path) throws InvalidParameterException {
		String pathPattern = "(/[a-zA-Z0-9_-]+)+/";
		Pattern p = Pattern.compile(pathPattern);
		Matcher m = p.matcher(path);

		boolean isMatch = m.matches();
		if (!isMatch) {
			throw new InvalidParameterException("Mismatched pattern in path.");
		}
	}

	private String loggedInResult(String cookie) throws InvalidParameterException {
		String[] cookieParts = cookie.split("=");
		if (cookieParts.length != 2) {
			throw new InvalidParameterException("Invalid cookie: number of cookie parts: " + cookieParts.length);
		}
		if (!cookieParts[0].equals("sessionid")) {
			throw new InvalidParameterException("Invalid cookie name: " + cookieParts[0]);
		} else {
			return cookieParts[1];
		}
	}

	private String changeFolderNameToSend(String newName) throws InvalidParameterException {
		String pathPattern = "[a-zA-Z0-9_-]+";
		Pattern p = Pattern.compile(pathPattern);
		Matcher m = p.matcher(newName);
		boolean isMatch = m.matches();
		if (!isMatch) {
			throw new InvalidParameterException("Mismatched pattern in path.");
		}
		return newName;
	}

	// System.out.println("code: " + jsonResponse.getCode());
	// System.out.println("body:" + jsonResponse.getBody());
	// System.out.println("headers: " + jsonResponse.getHeaders());
	// System.out.println("raw body: " + jsonResponse.getRawBody());
}
