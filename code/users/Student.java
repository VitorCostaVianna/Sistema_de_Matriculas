package users;

import java.util.ArrayList;
import java.util.List;

import subject.Discipline;


public class Student extends User {

    private List<Discipline> disciplines;

    private boolean enrolmentActive;

    public Student(String name, String email, String password) {
        super(name, email, password);
        setEnrolmentActive(true);
        disciplines = new ArrayList<>();
        try {
            java.io.File studentFile = new java.io.File("student_" + name.replaceAll("\\s+", "_") + ".txt");
            if (studentFile.createNewFile()) {
                java.io.FileWriter writer = new java.io.FileWriter(studentFile);
                writer.write("Nome: " + name + "\nEmail: " + email + "\n");
                writer.close();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void enrolCourse(Discipline discipline) {
        if (isEnrolmentActive()) {
            this.disciplines.add(discipline);
            discipline.getStudents().add(this);
        }
    }

    public void enrolOptionaldiscipline(Discipline discipline) {
        if (isEnrolmentActive()) {
            this.disciplines.add(discipline);
            discipline.getStudents().add(this);
        }
    }

    public void canceldiscipline(Discipline discipline) {
        if (isEnrolmentActive()) {
            this.disciplines.remove(discipline);
            discipline.getStudents().remove(this);
        }
    }

    public boolean isEnrolmentActive() {
        return enrolmentActive;
    }

    public void setEnrolmentActive(boolean enrolmentActive) {
        this.enrolmentActive = enrolmentActive;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }
}
