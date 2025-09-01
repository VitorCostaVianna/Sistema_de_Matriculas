package users;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import subject.Discipline;

public class Teacher extends User{
    private List<Discipline> disciplines;

    public Teacher(String name, String email, String password) {
        super(name, email, password);
        this.disciplines = new ArrayList<>();
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

    public void salvarEmArquivo() {
    try {
        File teacherFile = new File("Teachers.txt");
        FileWriter writer = new FileWriter(teacherFile, true); // append mode
        writer.write("Nome: " + getName() + ", Email: " + getEmail() + ", Password: " + getPassword() + "\n");
        writer.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
