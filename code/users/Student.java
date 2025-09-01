package users;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import services.Enrolment;


public class Student extends User {

    private Enrolment enrolment;
    private boolean enrolmentActive;

    public Student(String name, String email, String password) {
        this(name, email, password, true);
        try {
            java.io.File file = new java.io.File("AlunosMaterias.txt"); // ou "estudantes.txt"
            boolean existe = false;
            if (file.exists()) {
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("email: " + email)) {
                        existe = true;
                        break;
                    }
                }
                reader.close();
            }
            if (!existe) {
                java.io.FileWriter writer = new java.io.FileWriter(file, true); // append
                writer.write("nome: " + name + ", email: " + email + ", obrigatorias: 0, opcionais: 0" + System.lineSeparator());
                writer.close();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    // Novo construtor: salva no arquivo apenas se for cadastro
    public Student(String name, String email, String password, boolean salvarArquivo) {
        super(name, email, password);
        setEnrolmentActive(true);
        if (salvarArquivo) {
            try {
                File studentFile = new File("Students.txt");
                FileWriter writer = new FileWriter(studentFile, true); // append mode
                writer.write("Nome: " + name + ", Email: " + email + ", Password: " + password + ", Enrol Active: " + enrolmentActive + "\n");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    

    public boolean isEnrolmentActive() {
        return enrolmentActive;
    }

    public void setEnrolmentActive(boolean enrolmentActive) {
        this.enrolmentActive = enrolmentActive;
    }

    public Enrolment getEnrolment() {
        return enrolment;
    }

}
