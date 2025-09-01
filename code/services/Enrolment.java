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

    private void atualizarArquivoAlunoUnico(Student student, int obrigatorias, int opcionais) {
        try {
            java.io.File file = new java.io.File("AlunosMaterias.txt");
            List<String> linhas = new ArrayList<>();
            boolean alunoEncontrado = false;
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("email: " + student.getEmail())) {
                    // Atualiza a linha do aluno
                    line = "nome: " + student.getName() + ", email: " + student.getEmail()
                            + ", obrigatorias: " + obrigatorias + ", opcionais: " + opcionais;
                    alunoEncontrado = true;
                }
                linhas.add(line);
            }
            reader.close();

            // Se o aluno não foi encontrado, adiciona uma nova linha
            if (!alunoEncontrado) {
                linhas.add("nome: " + student.getName() + ", email: " + student.getEmail()
                        + ", obrigatorias: " + obrigatorias + ", opcionais: " + opcionais);
            }

            java.io.FileWriter writer = new java.io.FileWriter(file, false); // sobrescreve tudo
            for (String l : linhas) {
                writer.write(l + System.lineSeparator());
            }
            writer.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void enrolCourse(Discipline discipline, Student student) {
        int[] quantidades = buscarQuantidadesAluno(student.getEmail());
        int obrigatorias = quantidades[0];
        int opcionais = quantidades[1];

        if (obrigatorias >= 4) {
            System.out.println("Limite de 4 disciplinas obrigatórias atingido.");
            return;
        }
        if (disciplines.contains(discipline)) {
            System.out.println("Aluno já está matriculado nesta disciplina obrigatória.");
            return;
        }
        disciplines.add(discipline);
        obrigatorias++; // incrementa manualmente
        atualizarArquivoAlunoUnico(student, obrigatorias, opcionais);
        registrarMatriculaAluno(student, discipline, "obrigatoria");
        PaymentSystem.getInstancia().gerarCobranca(student, discipline);
        System.out.println("Matrícula obrigatória realizada!");
    }

    public void enrolOptionaldiscipline(Discipline discipline) {
        int[] quantidades = buscarQuantidadesAluno(student.getEmail());
        int obrigatorias = quantidades[0];
        int opcionais = quantidades[1];

        if (opcionais >= 2) {
            System.out.println("Limite de 2 disciplinas opcionais atingido.");
            return;
        }
        if (disciplines.contains(discipline)) {
            System.out.println("Aluno já está matriculado nesta disciplina opcional.");
            return;
        }
        disciplines.add(discipline);
        opcionais++; // incrementa manualmente
        atualizarArquivoAlunoUnico(student, obrigatorias, opcionais);
        registrarMatriculaAluno(student, discipline, "opcional");
        PaymentSystem.getInstancia().gerarCobranca(student, discipline);
        System.out.println("Matrícula opcional realizada!");
    }

    public void canceldiscipline(Discipline discipline) {
        if (student.isEnrolmentActive() && this.disciplines.contains(discipline)) {
            this.disciplines.remove(discipline);
            PaymentSystem.getInstancia().removerCobranca(student, discipline); 
            System.out.println("Disciplina cancelada e cobrança atualizada!");
        } else {
            System.out.println("O aluno não está matriculado nesta disciplina.");
        }
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
                                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                                        .ofPattern("dd/MM/yyyy");
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

    private int[] buscarQuantidadesAluno(String email) {
        int obrigatorias = 0;
        int opcionais = 0;
        try {
            java.io.File file = new java.io.File("AlunosMaterias.txt");
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("email: " + email)) {
                    String[] partes = line.split(",");
                    for (String parte : partes) {
                        parte = parte.trim();
                        if (parte.startsWith("obrigatorias:")) {
                            obrigatorias = Integer.parseInt(parte.replace("obrigatorias:", "").trim());
                        }
                        if (parte.startsWith("opcionais:")) {
                            opcionais = Integer.parseInt(parte.replace("opcionais:", "").trim());
                        }
                    }
                    break;
                }
            }
            reader.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return new int[] { obrigatorias, opcionais };
    }

    private void registrarMatriculaAluno(Student student, Discipline discipline, String tipo) {
        try {
            java.io.File file = new java.io.File("AlunoDisciplinas.txt");
            java.io.FileWriter writer = new java.io.FileWriter(file, true); // append
            writer.write("nome: " + student.getName()
                    + ", email: " + student.getEmail()
                    + ", disciplina: " + discipline.getName()
                    + ", tipo: " + tipo
                    + System.lineSeparator());
            writer.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
