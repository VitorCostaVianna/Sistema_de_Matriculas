package services;

import java.time.LocalDate;
import java.util.List;

import java.util.UUID;

import subject.Discipline;
import users.Student;

public class Enrolment {
    private String id = UUID.randomUUID().toString();
    private Student student;
    private LocalDate enrolPeriod;
    private List<Discipline> disciplines;

    public Enrolment(Student student, LocalDate enrolPeriod) {
        this.student = student;
        this.enrolPeriod = enrolPeriod;
        try {
            java.io.File enrolmentFile = new java.io.File("Enrolments.txt");
            java.io.FileWriter writer = new java.io.FileWriter(enrolmentFile, true); // append mode
            writer.write("Id da matrícula: " + id
                + ", Nome: " + student.getName()
                + ", Email: " + student.getEmail()
                + ", Período: " + enrolPeriod
                + System.lineSeparator());
            writer.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void setEnrolPeriod(LocalDate enrolPeriod) {
        this.enrolPeriod = enrolPeriod;
    }

    public void enrolCourse(Discipline discipline) {
        if (student.isEnrolmentActive() && verifyenrolPeriodDate(enrolPeriod)) {
            this.disciplines.add(discipline);
            try {
                java.io.File file = new java.io.File("AlunoDisciplinas.txt");
                java.io.FileWriter writer = new java.io.FileWriter(file, true);
                writer.write("Aluno: " + student.getName()
                    + ", Email: " + student.getEmail()
                    + ", Disciplina: " + discipline.getName()
                    + ", Período: " + (enrolPeriod != null ? enrolPeriod.toString() : "")
                    + System.lineSeparator());
                writer.close();
                System.out.println("Aluno registrado em AlunoDisciplinas.txt!");
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aluno NÃO registrado: matrícula inativa ou período inválido.");
        }
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
