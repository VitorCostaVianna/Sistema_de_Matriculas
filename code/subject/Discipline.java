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

}