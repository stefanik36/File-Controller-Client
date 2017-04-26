package pl.edu.agh.kis.model;

public class Folder extends Data{

    private final Integer folderId;
    private final String  name;
    private final String  pathLower;
    private final String  pathDisplay;
    private final Integer parentFolderId;
    private final String  serverCreatedAt;
    private final Integer ownerId;

    public Folder(Folder value) {
        this.folderId = value.folderId;
        this.name = value.name;
        this.pathLower = value.pathLower;
        this.pathDisplay = value.pathDisplay;
        this.parentFolderId = value.parentFolderId;
        this.serverCreatedAt = value.serverCreatedAt;
        this.ownerId = value.ownerId;
    }

    public Folder(
        Integer folderId,
        String  name,
        String  pathLower,
        String  pathDisplay,
        Integer parentFolderId,
        String  serverCreatedAt,
        Integer ownerId
    ) {
        this.folderId = folderId;
        this.name = name;
        this.pathLower = pathLower;
        this.pathDisplay = pathDisplay;
        this.parentFolderId = parentFolderId;
        this.serverCreatedAt = serverCreatedAt;
        this.ownerId = ownerId;
    }

    public Integer getFolderId() {
        return this.folderId;
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

    public Integer getParentFolderId() {
        return this.parentFolderId;
    }

    public String getServerCreatedAt() {
        return this.serverCreatedAt;
    }

    public Integer getOwnerId() {
        return this.ownerId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Folder (");

        sb.append(folderId);
        sb.append(", ").append(name);
        sb.append(", ").append(pathLower);
        sb.append(", ").append(pathDisplay);
        sb.append(", ").append(parentFolderId);
        sb.append(", ").append(serverCreatedAt);
        sb.append(", ").append(ownerId);

        sb.append(")");
        return sb.toString();
    }
}