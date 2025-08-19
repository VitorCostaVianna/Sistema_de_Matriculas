package users;

import java.util.UUID;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        setId(generateId());
        setName(name);
        setEmail(email);
        setPassword(password);
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean login(String email, String password) {
       // TODO: Implement login logic
       return false;
    }

    public String recoverPassword(String email){
        // TODO: Implement password recovery logic
        return "Password recovery link sent to " + email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}