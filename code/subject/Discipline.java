package subject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import users.Student;
import users.Teacher;

public class Discipline {
	private String id;
    private String name;
    private Long creditsNumber;
	private List<Student> students;
	private Teacher teacher;
	public static final int MAX_STUDENTS = 60; 
	public static final int MIN_STUDENTS = 3;
	private Status status;
	private boolean required;

	public Discipline(Teacher teacher, boolean required, String name, Long creditsNumber) {
        this.setTeacher(teacher);
        this.setStatus(Status.INDEFINITE);
        students = new ArrayList<>();
		this.setId(generateId());
		this.required = required;
		this.creditsNumber = creditsNumber;
		this.name = name;

		// Adiciona a disciplina ao arquivo
		try {
			java.io.File file = new java.io.File("Disciplines.txt");
			java.io.FileWriter writer = new java.io.FileWriter(file, true); // append mode
			writer.write("Id: " + this.id
				+ ", Nome: " + this.name
				+ ", Créditos: " + this.creditsNumber
				+ ", Obrigatória: " + this.required
				+ ", Professor: " + (teacher != null ? teacher.getName() : "N/A")
				+ System.lineSeparator());
			writer.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
    }

	// Construtor alternativo para leitura do arquivo (não grava no arquivo)
	public Discipline(Teacher teacher, boolean required, String name, Long creditsNumber, boolean fromFile) {
        this.setTeacher(teacher);
        this.setStatus(Status.INDEFINITE);
        students = new ArrayList<>();
		this.setId(generateId());
		this.required = required;
		this.creditsNumber = creditsNumber;
		this.name = name;
		// Não grava no arquivo!
    }

	public String getId() {
		return id;
	}

    public void setId(String id) {
        this.id = id;
    }

     private String generateId() {
        return UUID.randomUUID().toString();
    }

	public List<Student> getStudents() {
		return students;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public Status getStatus() {
		return status;
	}

	public boolean isRequired() {
		return required;
	}

	public boolean DisciplineViabilityPolicy() {
		int enrolled = students != null ? students.size() : 0;
		return enrolled >= MIN_STUDENTS && enrolled <= MAX_STUDENTS;
	}

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreditsNumber() {
        return creditsNumber;
    }

    public void setCreditsNumber(Long creditsNumber) {
        this.creditsNumber = creditsNumber;
    }


    public static List<Discipline> getDisciplinasOpcionaisFromFile() {
        List<Discipline> opcionais = new ArrayList<>();
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("Disciplines.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                // Exemplo de linha: Id: ..., Nome: ..., Créditos: ..., Obrigatória: false, Professor: ...
                if (line.contains("Obrigatória: false")) {
                    // Extrai os dados necessários (aqui só o nome, mas pode adaptar para mais campos)
                    String[] partes = line.split(",");
                    String nome = "";
                    Long creditos = 0L;
                    boolean required = false;
                    for (String parte : partes) {
                        if (parte.trim().startsWith("Nome:")) {
                            nome = parte.split(":")[1].trim();
                        }
                        if (parte.trim().startsWith("Créditos:")) {
                            creditos = Long.parseLong(parte.split(":")[1].trim());
                        }
                    }
                    // Cria disciplina apenas com nome e créditos (professor pode ser null)
                    opcionais.add(new Discipline(null, required, nome, creditos, true));
                }
            }
            reader.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return opcionais;
    }

    public static List<Discipline> getDisciplinasObrigatoriasFromFile() {
        List<Discipline> obrigatorias = new ArrayList<>();
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader("Disciplines.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Obrigatória: true")) {
                    String[] partes = line.split(",");
                    String nome = "";
                    Long creditos = 0L;
                    boolean required = true;
                    for (String parte : partes) {
                        if (parte.trim().startsWith("Nome:")) {
                            nome = parte.split(":")[1].trim();
                        }
                        if (parte.trim().startsWith("Créditos:")) {
                            creditos = Long.parseLong(parte.split(":")[1].trim());
                        }
                    }
                    obrigatorias.add(new Discipline(null, required, nome, creditos, true));
                }
            }
            reader.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return obrigatorias;
    }

}