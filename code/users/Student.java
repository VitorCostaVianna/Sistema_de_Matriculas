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
