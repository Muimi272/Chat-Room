import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String password;
    private static final long serialVersionUID = 1L;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
