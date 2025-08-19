package users;

import java.util.Date;

import subject.Discipline;

public class Secretary extends User {

    public Secretary(String name, String email, String password) {
        super(name, email, password);
    }

    public void addDisciplines(Teacher teacher, boolean required, String name, Long creditsNumber) {
        Discipline discipline = new Discipline(teacher, required, name, creditsNumber);
        try (java.io.FileWriter writer = new java.io.FileWriter("Disciplines.txt", true)) {
            writer.write(discipline.getName() + System.lineSeparator());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void setEnrolPeriod(Date startDate, Date endDate) {
       
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
            writer.write("Período: " + period + System.lineSeparator());

            try (java.io.BufferedReader disciplineReader = new java.io.BufferedReader(new java.io.FileReader("Disciplines.txt"))) {
                writer.write("Disciplines:" + System.lineSeparator());
                String disciplineLine;
                while ((disciplineLine = disciplineReader.readLine()) != null) {
                    writer.write("  - " + disciplineLine + System.lineSeparator());
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            try (java.io.BufferedReader teacherReader = new java.io.BufferedReader(new java.io.FileReader("teachers.txt"))) {
                writer.write("Professores:" + System.lineSeparator());
                String teacherLine;
                while ((teacherLine = teacherReader.readLine()) != null) {
                    writer.write("  - " + teacherLine + System.lineSeparator());
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            try (java.io.BufferedReader studentReader = new java.io.BufferedReader(new java.io.FileReader("students.txt"))) {
                writer.write("Alunos:" + System.lineSeparator());
                String studentLine;
                while ((studentLine = studentReader.readLine()) != null) {
                    writer.write("  - " + studentLine + System.lineSeparator());
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

            writer.write(System.lineSeparator());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

}
