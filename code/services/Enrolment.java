package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

import subject.Discipline;
import users.Student;

public class Enrolment {
    private String id = UUID.randomUUID().toString();
    private Student student;
    private List<Discipline> disciplines;

    private int obrigatoriasMatriculadas = 0;
    private int opcionaisMatriculadas = 0;

    public Enrolment(Student student) {
        this.student = student;
        this.disciplines = new ArrayList<>();
        try {
            java.io.File enrolmentFile = new java.io.File("Enrolments.txt");
            java.io.FileWriter writer = new java.io.FileWriter(enrolmentFile, true); // append mode
            writer.write("Id da matrícula: " + id
                + ", Nome: " + student.getName()
                + ", Email: " + student.getEmail()
                + System.lineSeparator());
            writer.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void enrolCourse(Discipline discipline, Student student) {
        if (obrigatoriasMatriculadas >= 4) {
            System.out.println("Limite de 4 disciplinas obrigatórias atingido.");
            return;
        }
        if (disciplines.contains(discipline)) {
            System.out.println("Aluno já está matriculado nesta disciplina obrigatória.");
            return;
        }
        disciplines.add(discipline);
        obrigatoriasMatriculadas++; // Incrementa ao matricular
        try {
            java.io.File file = new java.io.File("AlunoDisciplinas.txt");
            java.io.FileWriter writer = new java.io.FileWriter(file, true);
            writer.write("Aluno: " + student.getName()
                + ", Email: " + student.getEmail()
                + ", Disciplina: " + discipline.getName()
                + System.lineSeparator());
            writer.close();
            System.out.println("Aluno registrado em AlunoDisciplinas.txt!");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        System.out.println("Matrícula obrigatória realizada!");
    }

    public void enrolOptionaldiscipline(Discipline discipline) {
        if (opcionaisMatriculadas >= 2) {
            System.out.println("Limite de 2 disciplinas opcionais atingido.");
            return;
        }
        if (disciplines.contains(discipline)) {
            System.out.println("Aluno já está matriculado nesta disciplina opcional.");
            return;
        }
        disciplines.add(discipline);
        opcionaisMatriculadas++; // Incrementa ao matricular
        try {
            java.io.File file = new java.io.File("AlunoDisciplinas.txt");
            java.io.FileWriter writer = new java.io.FileWriter(file, true);
            writer.write("Aluno: " + student.getName()
                + ", Email: " + student.getEmail()
                + ", Disciplina: " + discipline.getName()
                + ", Tipo: Opcional"
                + System.lineSeparator());
            writer.close();
            System.out.println("Aluno registrado em AlunoDisciplinas.txt!");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        System.out.println("Matrícula opcional realizada!");
    }

    public void canceldiscipline(Discipline discipline) {
        if (student.isEnrolmentActive()) {
            this.disciplines.remove(discipline);
        }

        // TODO - alterar satatus do curso no arquivo
    }

    private boolean verifyenrolPeriodDate() {
    try {
        java.io.File file = new java.io.File("EnrolPeriod.txt");
        
        if (!file.exists()) {
            System.out.println("Arquivo EnrolPeriod.txt não encontrado!");
            return false;
        }
        
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
        String line;
        LocalDate finalDate = null;
        
        // Procura pela linha que contém "Data Final"
        while ((line = reader.readLine()) != null) {
            if (line.toLowerCase().contains("data final")) {
                // Extrai a data da linha
                // Exemplo: "Data Final: 2024-12-31" ou "Data Final: 31/12/2024"
                String[] parts = line.split(":");
                if (parts.length > 1) {
                    String dateStr = parts[1].trim();
                    try {
                        // Tenta formato ISO (yyyy-MM-dd)
                        finalDate = LocalDate.parse(dateStr);
                    } catch (java.time.format.DateTimeParseException e) {
                        try {
                            // Tenta formato brasileiro (dd/MM/yyyy)
                            java.time.format.DateTimeFormatter formatter = 
                                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            finalDate = LocalDate.parse(dateStr, formatter);
                        } catch (java.time.format.DateTimeParseException e2) {
                            System.out.println("Erro ao converter data: " + dateStr);
                        }
                    }
                }
                break;
            }
        }
        reader.close();
        
        if (finalDate == null) {
            System.out.println("Data Final não encontrada no arquivo!");
            return false;
        }
        
        // Retorna true se a data atual for anterior à data final
        return LocalDate.now().isBefore(finalDate);
        
    } catch (java.io.IOException e) {
        System.out.println("Erro ao ler arquivo EnrolPeriod.txt: " + e.getMessage());
        return false;
    }
}




}
