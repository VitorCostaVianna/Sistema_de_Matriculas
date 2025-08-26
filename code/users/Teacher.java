package users;

import java.io.File;
import java.io.FileWriter;
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
            File teacherFile = new File("Teachers.txt");
            FileWriter writer = new FileWriter(teacherFile, true); // append mode
            writer.write("Nome: " + name + ", Email: " + email + ", Password: " + password + "\n");
            writer.close();
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
