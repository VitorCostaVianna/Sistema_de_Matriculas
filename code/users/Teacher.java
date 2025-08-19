package users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import subject.Discipline;

public class Teacher extends User{
    private List<Discipline> disciplines;

    public Teacher(String name, String email, String password) {
        super(name, email, password);
        this.disciplines = new ArrayList<>();
         try {
            java.io.File teacherFile = new java.io.File("teacher_" + name.replaceAll("\\s+", "_") + ".txt");
            if (teacherFile.createNewFile()) {
                java.io.FileWriter writer = new java.io.FileWriter(teacherFile);
                writer.write("Nome: " + name + "\nEmail: " + email + "\n");
                writer.close();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public HashMap<Discipline, List<Student>> viewStudents() {
      HashMap<Discipline, List<Student>> mapStudentsPerDiscipline = new HashMap<Discipline, List<Student>>();
      for (Discipline discipline : disciplines) {
          mapStudentsPerDiscipline.put(discipline, discipline.getStudents());
      }
      return mapStudentsPerDiscipline;
    }
}
