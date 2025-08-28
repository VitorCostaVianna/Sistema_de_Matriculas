import users.Student;
import users.Teacher;
import users.Secretary;
import users.User;
import services.Enrolment;
import subject.Discipline;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Student student = null;
        Teacher teacher = null;
        Secretary secretary = null;
        Enrolment enrolment = null;

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Criar estudante");
            System.out.println("2. Criar professor");
            System.out.println("3. Criar secretário");
            System.out.println("4. Login");
            System.out.println("5. Recuperar senha");
            System.out.println("6. Adicionar disciplina");
            System.out.println("7. Matricular estudante em disciplina");
            System.out.println("8. Adicionar currículo");
            System.out.println("9. Set período de matrícula"); 
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    System.out.print("Nome do estudante: ");
                    String snome = scanner.nextLine();
                    System.out.print("Email: ");
                    String semail = scanner.nextLine();
                    System.out.print("Senha: ");
                    String spass = scanner.nextLine();
                    student = new Student(snome, semail, spass);
                    System.out.println("Estudante criado!");
                    break;
                case 2:
                    System.out.print("Nome do professor: ");
                    String tnome = scanner.nextLine();
                    System.out.print("Email: ");
                    String temail = scanner.nextLine();
                    System.out.print("Senha: ");
                    String tpass = scanner.nextLine();
                    teacher = new Teacher(tnome, temail, tpass);
                    System.out.println("Professor criado!");
                    break;
                case 3:
                    System.out.print("Nome do secretário: ");
                    String cnome = scanner.nextLine();
                    System.out.print("Email: ");
                    String cemail = scanner.nextLine();
                    System.out.print("Senha: ");
                    String cpass = scanner.nextLine();
                    secretary = new Secretary(cnome, cemail, cpass);
                    System.out.println("Secretário criado!");
                    break;
                case 4:
                    System.out.print("Email: ");
                    String lemail = scanner.nextLine();
                    System.out.print("Senha: ");
                    String lpass = scanner.nextLine();
                    boolean login = User.loginFromAllFiles(lemail, lpass);
                    System.out.println(login ? "Login bem-sucedido!" : "Login falhou.");
                    break;
                case 5:
                    System.out.print("Email para recuperar senha: ");
                    String remail = scanner.nextLine();
                    System.out.println(new User("", "", "").recoverPassword(remail));
                    break;
                case 6:
                    if (secretary == null || teacher == null) {
                        System.out.println("Crie um secretário e um professor primeiro.");
                        break;
                    }
                    System.out.print("Nome da disciplina: ");
                    String dname = scanner.nextLine();
                    System.out.print("Obrigatória? (true/false): ");
                    boolean required = Boolean.parseBoolean(scanner.nextLine());
                    System.out.print("Créditos: ");
                    Long credits = Long.parseLong(scanner.nextLine());
                    secretary.addDisciplines(teacher, required, dname, credits);
                    System.out.println("Disciplina adicionada!");
                    break;
                case 7:

                    java.util.List<Student> alunos = new java.util.ArrayList<>();
                    try (java.io.BufferedReader reader = new java.io.BufferedReader(
                            new java.io.FileReader("Students.txt"))) {
                        String line;
                        int idx = 1;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(",");
                            String nome = "";
                            String email = "";
                            String senha = "";
                            Boolean enrolActive = null;
                            for (String part : parts) {
                                part = part.trim();
                                if (part.startsWith("Nome:"))
                                    nome = part.replace("Nome:", "").trim();
                                if (part.startsWith("Email:"))
                                    email = part.replace("Email:", "").trim();
                                if (part.startsWith("Password:"))
                                    senha = part.replace("Password:", "").trim();
                                if (part.startsWith("Enrol Active:"))
                                    enrolActive = Boolean.parseBoolean(part.replace("Enrol Active:", "").trim());
                            }
                            Student aluno;
                            if (enrolActive != null) {
                                aluno = new Student(nome, email, senha, false);
                                aluno.setEnrolmentActive(enrolActive);
                            } else {
                                aluno = new Student(nome, email, senha, false);
                            }
                            alunos.add(aluno);
                            System.out.println(idx + ". " + nome + " (" + email + ") - Enrol Active: "
                                    + (enrolActive != null ? enrolActive : "true"));
                            idx++;
                        }
                    } catch (java.io.IOException e) {
                        System.out.println("Erro ao ler lista de alunos.");
                        break;
                    }
                    if (alunos.isEmpty()) {
                        System.out.println("Nenhum aluno cadastrado.");
                        break;
                    }
                    System.out.print("Escolha o número do aluno para matrícula: ");
                    int escolha = Integer.parseInt(scanner.nextLine());
                    if (escolha < 1 || escolha > alunos.size()) {
                        System.out.println("Opção inválida.");
                        break;
                    }
                    Student alunoEscolhido = alunos.get(escolha - 1);
                    enrolment = new Enrolment(alunoEscolhido);
                    System.out.print("Nome da disciplina para matrícula: ");
                    String matDisc = scanner.nextLine();
                    Discipline disciplina = new Discipline(teacher, true, matDisc, 60L);
                    System.out.println("Enrol Perid: " + alunoEscolhido.isEnrolmentActive());
                    enrolment.enrolCourse(disciplina, alunoEscolhido);
                    System.out.println("Aluno matriculado!");
                    break;
                case 8:
                    if (secretary == null) {
                        System.out.println("Crie um secretário primeiro.");
                        break;
                    }
                    System.out.print("Período do currículo: ");
                    String periodo = scanner.nextLine();
                    secretary.addCurriculum(periodo);
                    System.out.println("Currículo adicionado!");
                    break;
                case 9:
                    if (secretary == null) {
                        System.out.println("Crie um secretário primeiro.");
                        break;
                    }
                    System.out.println("Digite a data final para matrícula (dd/MM/yyyy): ");
                    String dataFinal = scanner.nextLine();

                    try {
                        // Converte String para LocalDate
                        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                                .ofPattern("dd/MM/yyyy");
                        LocalDate dataFinalLocalDate = LocalDate.parse(dataFinal, formatter);

                        // Passa o LocalDate para o método
                        secretary.setEnrolPeriod(dataFinalLocalDate);

                        System.out.println("Data final de matrícula definida: " +
                                dataFinalLocalDate.format(formatter));

                    } catch (java.time.format.DateTimeParseException e) {
                        System.out.println("Formato de data inválido! Use o formato dd/MM/yyyy");
                        System.out.println("Exemplo: 31/12/2024");
                    }
                    break;
                case 0:
                    System.out.println("Saindo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}
