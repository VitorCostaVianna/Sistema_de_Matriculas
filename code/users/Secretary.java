package users;

import java.time.LocalDate;

import subject.Discipline;

public class Secretary extends User {

    public Secretary(String name, String email, String password) {
        super(name, email, password);
    }

    public void addDisciplines(Teacher teacher, boolean required, String name, Long creditsNumber) {
        Discipline discipline = new Discipline(teacher, required, name, creditsNumber);
        try (java.io.FileWriter writer = new java.io.FileWriter("Disciplines.txt", true)) {
            writer.write("Nome: " + discipline.getName()
                + ", Professor: " + (teacher != null ? teacher.getName() : "")
                + ", Obrigatória: " + required
                + ", Créditos: " + creditsNumber
                + System.lineSeparator());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void setEnrolPeriod(LocalDate endDate) {
        try (java.io.FileWriter writer = new java.io.FileWriter("EnrolPeriod.txt", true)) {
            writer.write("Data Final: " + endDate
                + System.lineSeparator());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
       
    }

    public void addStudent(String name, String email, String password) {
        Student student = new Student(name, email, password);
    }

    public void removeStudent(String email) {

    }

    public void updateStudent(String email, String newName, String newEmail) {
        // Logic to update a student's information
    }

    public void addCurriculum(String period) {
        try (java.io.FileWriter writer = new java.io.FileWriter("curriculum.txt", true)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Período: ").append(period);

            // Disciplines
            sb.append(" | Disciplinas: [");
            try (java.io.BufferedReader disciplineReader = new java.io.BufferedReader(new java.io.FileReader("Disciplines.txt"))) {
                String disciplineLine;
                boolean first = true;
                while ((disciplineLine = disciplineReader.readLine()) != null) {
                    if (!first) sb.append(", ");
                    sb.append(disciplineLine);
                    first = false;
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            sb.append("]");

            // Professores
            sb.append(" | Professores: [");
            try (java.io.BufferedReader teacherReader = new java.io.BufferedReader(new java.io.FileReader("Teachers.txt"))) {
                String teacherLine;
                boolean first = true;
                while ((teacherLine = teacherReader.readLine()) != null) {
                    if (!first) sb.append(", ");
                    sb.append(teacherLine);
                    first = false;
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            sb.append("]");

            // Alunos
            sb.append(" | Alunos: [");
            try (java.io.BufferedReader studentReader = new java.io.BufferedReader(new java.io.FileReader("Students.txt"))) {
                String studentLine;
                boolean first = true;
                while ((studentLine = studentReader.readLine()) != null) {
                    if (!first) sb.append(", ");
                    sb.append(studentLine);
                    first = false;
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            sb.append("]");

            sb.append(System.lineSeparator());
            writer.write(sb.toString());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

}
