package services;

import java.time.LocalDate;
import java.util.List;

import subject.Discipline;
import users.Student;

public class Enrolment {
    private String id;
    private Student student;
    private LocalDate enrolPeriod;
    private List<Discipline> disciplines;

    public Enrolment(Student student) {
        try {
            java.io.File enrolmentFile = new java.io.File(
                    "enrolStudent_" + student.getName().replaceAll("\\s+", "_") + ".txt");
            if (enrolmentFile.createNewFile()) {
                java.io.FileWriter writer = new java.io.FileWriter(enrolmentFile);
                writer.write("Id da matrícula: " + id +
                        "Nome: " + student.getName() +
                        "\nEmail: " + student.getEmail() +
                        "\n");
                writer.close();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void enrolCourse(Discipline discipline) {
        if (student.isEnrolmentActive() && verifyenrolPeriodDate(enrolPeriod)) {
            this.disciplines.add(discipline);
        }

        // PaymentSystem.getInstancia().gerarCobranca(student); - > notifica o sistema de pagamento 

        // TODO - adicionar curso no arquivo
    }

    public void enrolOptionaldiscipline(Discipline discipline) {
        if (student.isEnrolmentActive() && verifyenrolPeriodDate(enrolPeriod)) {
            this.disciplines.add(discipline);
        }

        // PaymentSystem.getInstancia().gerarCobranca(student); - > notifica o sistema de pagamento 

        // TODO - remover curso no arquivo
    }

    public void canceldiscipline(Discipline discipline) {
        if (student.isEnrolmentActive()) {
            this.disciplines.remove(discipline);
        }

        // TODO - alterar satatus do curso no arquivo
    }

    private boolean verifyenrolPeriodDate(LocalDate now) {
        return now.isBefore(enrolPeriod) ? true : false;
    }
}
