package pl.edu.agh.kis.model;

public class User {

    private final Integer id;
    private final String  userName;
    private final String  displayName;
    private final String  hashedPassword;

    public User(User value) {
        this.id = value.id;
        this.userName = value.userName;
        this.displayName = value.displayName;
        this.hashedPassword = value.hashedPassword;
    }

    public User(
        Integer id,
        String  userName,
        String  displayName,
        String  hashedPassword
    ) {
        this.id = id;
        this.userName = userName;
        this.displayName = displayName;
        this.hashedPassword = hashedPassword;
    }

    public Integer getId() {
        return this.id;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("User (");

        sb.append(id);
        sb.append(", ").append(userName);
        sb.append(", ").append(displayName);
        sb.append(", ").append(hashedPassword);

        sb.append(")");
        return sb.toString();
    }
}