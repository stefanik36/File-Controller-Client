package pl.edu.agh.kis.model;

public class File extends Data{
	private final Integer fileId;
	private final String name;
	private final String pathLower;
	private final String pathDisplay;
	private final Integer enclosingFolderId;
	private final Integer size;
	private final String serverCreatedAt;
	private final String serverChangedAt;
	private final Integer ownerId;

	public File(File value) {
        this.fileId = value.fileId;
        this.name = value.name;
        this.pathLower = value.pathLower;
        this.pathDisplay = value.pathDisplay;
        this.enclosingFolderId = value.enclosingFolderId;
        this.size = value.size;
        this.serverCreatedAt = value.serverCreatedAt;
        this.serverChangedAt = value.serverChangedAt;
        this.ownerId = value.ownerId;
    }

	public File(
        Integer fileId,
        String  name,
        String  pathLower,
        String  pathDisplay,
        Integer enclosingFolderId,
        Integer size,
        String  serverCreatedAt,
        String  serverChangedAt,
        Integer ownerId
    ) {
        this.fileId = fileId;
        this.name = name;
        this.pathLower = pathLower;
        this.pathDisplay = pathDisplay;
        this.enclosingFolderId = enclosingFolderId;
        this.size = size;
        this.serverCreatedAt = serverCreatedAt;
        this.serverChangedAt = serverChangedAt;
        this.ownerId = ownerId;
    }

	public Integer getFileId() {
		return this.fileId;
	}

	public String getName() {
		return this.name;
	}

	public String getPathLower() {
		return this.pathLower;
	}

	public String getPathDisplay() {
		return this.pathDisplay;
	}

	public Integer getEnclosingFolderId() {
		return this.enclosingFolderId;
	}

	public Integer getSize() {
		return this.size;
	}

	public String getServerCreatedAt() {
		return this.serverCreatedAt;
	}

	public String getServerChangedAt() {
		return this.serverChangedAt;
	}

	public Integer getOwnerId() {
		return this.ownerId;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("FileMetadata (");

		sb.append(fileId);
		sb.append(", ").append(name);
		sb.append(", ").append(pathLower);
		sb.append(", ").append(pathDisplay);
		sb.append(", ").append(enclosingFolderId);
		sb.append(", ").append(size);
		sb.append(", ").append(serverCreatedAt);
		sb.append(", ").append(serverChangedAt);
		sb.append(", ").append(ownerId);

		sb.append(")");
		return sb.toString();
	}
}
