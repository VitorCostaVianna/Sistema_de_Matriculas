package users;

import java.util.UUID;

public class User {
    // Autentica usuário verificando ambos os arquivos de usuários
    public static boolean loginFromAllFiles(String email, String password) {
        String[] files = {"Students.txt", "Teachers.txt", "Secretary.txt"};
        for (String file : files) {
            if (loginFromFile(email, password, file)) {
                return true;
            }
        }
        return false;
    }
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
        return this.email.equals(email) && this.password.equals(password);
    }

    public static boolean loginFromFile(String email, String password, String filePath) {
        String[] files = {"Students.txt", "Teachers.txt", "Secretary.txt"};
        for (String file : files) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String foundEmail = null;
                    String foundPassword = null;
                    for (String part : parts) {
                        part = part.trim();
                        if (part.startsWith("Email:")) {
                            foundEmail = part.replace("Email:", "").trim();
                        }
                        if (part.startsWith("Password:")) {
                            foundPassword = part.replace("Password:", "").trim();
                        }
                    }
                    if (foundEmail != null && foundPassword != null && foundEmail.equals(email) && foundPassword.equals(password)) {
                        return true;
                    }
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String recoverPassword(String email){
        String[] files = {"Students.txt", "Teachers.txt", "Secretary.txt"};
        for (String file : files) {
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String foundEmail = null;
                    String foundPassword = null;
                    for (String part : parts) {
                        part = part.trim();
                        if (part.startsWith("Email:")) {
                            foundEmail = part.replace("Email:", "").trim();
                        }
                        if (part.startsWith("Password:")) {
                            foundPassword = part.replace("Password:", "").trim();
                        }
                    }
                    if (foundEmail != null && foundEmail.equals(email)) {
                        return "Sua senha é: " + (foundPassword != null ? foundPassword : "não encontrada");
                    }
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return "Email não encontrado.";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}