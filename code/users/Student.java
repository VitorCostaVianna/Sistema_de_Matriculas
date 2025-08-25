package users;

import services.Enrolment;


public class Student extends User {

    private Enrolment enrolment;
    private boolean enrolmentActive;

    public Student(String name, String email, String password) {
        super(name, email, password);
        setEnrolmentActive(true);
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
