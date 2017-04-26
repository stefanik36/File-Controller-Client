package pl.edu.agh.kis.client;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import pl.edu.agh.kis.exceptions.ServerException;
import pl.edu.agh.kis.model.Data;
import pl.edu.agh.kis.model.Folder;

public class FolderLogic extends DataLogic {

	protected FolderLogic() {
		this(null);
	}

	public FolderLogic(String sessionId) {
		this.sessionId = sessionId;
	}
	
	@Override
	public Data getMetadata(Path path) throws ServerException, UnirestException, UnsupportedEncodingException {
		HttpResponse<String> jsonResponse = getMetadataResponse(path);
		checkCode(jsonResponse.getCode());
		return gson.fromJson(jsonResponse.getBody(), Folder.class);
	}

	@Override
	public Data rename(Path path, String newName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data move(Path oldPath, Path newPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Data delete(Path path) {
		// TODO Auto-generated method stub
		return null;
	}

}
