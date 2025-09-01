import users.Student;
import users.Teacher;
import users.Secretary;
import users.User;
import services.Enrolment;
import subject.Discipline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static List<Discipline> todasDisciplinas = new ArrayList<>();

    // guarda o tipo do usuário após login ("student", "teacher", "secretary")
    private static String userRole = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Student student = null;
        Teacher teacher = null;
        Secretary secretary = null;
        Enrolment enrolment = null;

        while (true) {
            System.out.println("\n--- MENU ---");

            // ALTERAÇÃO: quando não logado, mostra somente os cases 1,2,3,4,5
            if (userRole == null) {
                System.out.println("1. Criar estudante");
                System.out.println("2. Criar professor");
                System.out.println("3. Criar secretário");
                System.out.println("4. Login");
                System.out.println("5. Recuperar senha");
                // Obs: case 0 (sair) existe mas não é exibido aqui como você solicitou
            } else if (userRole.equals("student")) {
                // Student -> recuperar senha, matricular, sair
                System.out.println("5. Recuperar senha");
                System.out.println("7. Matricular estudante em disciplina");
                System.out.println("0. Sair");
            } else if (userRole.equals("teacher")) {
                // Teacher -> recuperar senha, buscar alunos, sair
                System.out.println("5. Recuperar senha");
                System.out.println("10. Buscar alunos por disciplina: ");
                System.out.println("0. Sair");
            } else if (userRole.equals("secretary")) {
                // Secretary -> pode ver todas as opções (como antes)
                System.out.println("1. Criar estudante");
                System.out.println("2. Criar professor");
                System.out.println("3. Criar secretário");
                System.out.println("4. Login");
                System.out.println("5. Recuperar senha");
                System.out.println("6. Adicionar disciplina");
                System.out.println("7. Matricular estudante em disciplina");
                System.out.println("8. Adicionar currículo");
                System.out.println("9. Set período de matrícula");
                System.out.println("10. Buscar alunos por disciplina: ");
                System.out.println("11. Verificar disponibilidade das disciplinas: ");
                System.out.println("0. Sair");
            } else {
                System.out.println("0. Sair");
            }

            System.out.print("Escolha uma opção: ");
            int opcao;
            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número.");
                continue;
            }

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
                    teacher.salvarEmArquivo();
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
                    if (login) {
                        System.out.print("Tipo de usuário (student/teacher/secretary): ");
                        String tipo = scanner.nextLine().trim().toLowerCase();
                        if (tipo.equals("student") || tipo.equals("teacher") || tipo.equals("secretary")) {
                            userRole = tipo;
                            System.out.println("Login bem-sucedido como " + tipo + "!");
                            if (tipo.equals("secretary")) {
                                String nomeTemp = lemail;
                                int at = lemail.indexOf("@");
                                if (at > 0)
                                    nomeTemp = lemail.substring(0, at);

                                secretary = new Secretary(nomeTemp, lemail, lpass);
                            }
                        } else {
                            System.out.println("Tipo inválido. Continuando sem definir role.");
                        }
                    } else {
                        System.out.println("Login falhou.");
                    }
                    break;

                case 5:
                    System.out.print("Email para recuperar senha: ");
                    String remail = scanner.nextLine();
                    System.out.println(new User("", "", "").recoverPassword(remail));
                    break;
                case 6:
                    // remover a exigência de 'teacher' em memória — vamos escolher de Teachers.txt
                    if (secretary == null) {
                        System.out.println("Crie um secretário primeiro.");
                        break;
                    }

                    // Lê Teachers.txt e popula uma lista de Teacher
                    java.util.List<Teacher> teachers = new java.util.ArrayList<>();
                    try (BufferedReader tReader = new BufferedReader(new FileReader("Teachers.txt"))) {
                        String tLine;
                        while ((tLine = tReader.readLine()) != null) {
                            if (tLine.trim().isEmpty())
                                continue;
                            String nome = "";
                            String email = "";
                            String senha = "";
                            String[] parts = tLine.split(",");
                            for (String p : parts) {
                                p = p.trim();
                                String lower = p.toLowerCase();
                                if (lower.startsWith("nome:")) {
                                    String[] kv = p.split(":", 2);
                                    if (kv.length == 2)
                                        nome = kv[1].trim();
                                } else if (lower.startsWith("email:")) {
                                    String[] kv = p.split(":", 2);
                                    if (kv.length == 2)
                                        email = kv[1].trim();
                                } else if (lower.startsWith("password:") || lower.startsWith("senha:")) {
                                    String[] kv = p.split(":", 2);
                                    if (kv.length == 2)
                                        senha = kv[1].trim();
                                }
                            }
                            if (nome.isEmpty()) {
                                nome = tLine.trim();
                            }
                            // cria Teacher com o que foi encontrado (senha pode ser vazia)
                            teachers.add(new Teacher(nome, email, senha));
                        }
                    } catch (IOException e) {
                        System.out.println("Erro ao ler Teachers.txt: " + e.getMessage());
                        break;
                    }

                    if (teachers.isEmpty()) {
                        System.out.println("Nenhum professor cadastrado em Teachers.txt. Crie professores primeiro.");
                        break;
                    }

                    // Mostra a lista de professores e pede seleção
                    System.out.println("Professores disponíveis:");
                    for (int i = 0; i < teachers.size(); i++) {
                        Teacher t = teachers.get(i);
                        String displayName;
                        try {
                            displayName = t.getName() + " (" + t.getEmail() + ")";
                        } catch (Exception ex) {
                            // caso a classe Teacher não tenha getName/getEmail exposto, cai aqui
                            displayName = t.toString();
                        }
                        System.out.println((i + 1) + ". " + displayName);
                    }

                    System.out.print("Escolha o número do professor para atribuir à disciplina: ");
                    int escolhaProf;
                    try {
                        escolhaProf = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException nfe) {
                        System.out.println("Entrada inválida.");
                        break;
                    }
                    if (escolhaProf < 1 || escolhaProf > teachers.size()) {
                        System.out.println("Opção inválida.");
                        break;
                    }
                    Teacher professorEscolhido = teachers.get(escolhaProf - 1);

                    // Agora coleta os dados da disciplina normalmente
                    System.out.print("Nome da disciplina: ");
                    String dname = scanner.nextLine();
                    System.out.print("Disciplina obrigatória? (true/false): ");
                    boolean required;
                    try {
                        required = Boolean.parseBoolean(scanner.nextLine());
                    } catch (Exception e) {
                        System.out.println("Valor inválido para obrigatório. Use true ou false.");
                        break;
                    }
                    System.out.print("Número de créditos: ");
                    Long creditsNumber;
                    try {
                        creditsNumber = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Número de créditos inválido.");
                        break;
                    }

                    Discipline novaDisciplina = new Discipline(professorEscolhido, required, dname, creditsNumber);
                    todasDisciplinas.add(novaDisciplina);
                    System.out.println("Disciplina adicionada com professor: " + professorEscolhido.getName());
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

                    // Pergunta ao usuário:
                    String tipoDisciplina = "";
                    while (true) {
                        System.out.print(
                                "Deseja matricular em disciplina 'obrigatoria' ou 'opcional'? Digite exatamente: ");
                        tipoDisciplina = scanner.nextLine().trim().toLowerCase();
                        if (tipoDisciplina.equals("obrigatoria") || tipoDisciplina.equals("opcional")) {
                            break;
                        }
                        System.out.println("Opção inválida! Digite apenas 'obrigatoria' ou 'opcional'.");
                    }

                    if (tipoDisciplina.equals("obrigatoria")) {
                        int i = 1;
                        List<Discipline> obrigatorias = Discipline.getDisciplinasObrigatoriasFromFile();
                        for (Discipline d : obrigatorias) {
                            System.out.println(i + ". " + d.getName());
                            i++;
                        }
                        System.out.print("Escolha o número da disciplina para matrícula: ");
                        int escolhaDisc = Integer.parseInt(scanner.nextLine());
                        if (escolhaDisc < 1 || escolhaDisc > obrigatorias.size()) {
                            System.out.println("Opção inválida.");
                            break; // Volta ao menu principal
                        }
                        Discipline disciplinaEscolhida = obrigatorias.get(escolhaDisc - 1);
                        enrolment.enrolCourse(disciplinaEscolhida, alunoEscolhido);
                    } else {
                        int i = 1;
                        List<Discipline> opcionais = Discipline.getDisciplinasOpcionaisFromFile();
                        for (Discipline d : opcionais) {
                            System.out.println(i + ". " + d.getName());
                            i++;
                        }
                        System.out.print("Escolha o número da disciplina para matrícula: ");
                        int escolhaDisc = Integer.parseInt(scanner.nextLine());
                        if (escolhaDisc < 1 || escolhaDisc > opcionais.size()) {
                            System.out.println("Opção inválida.");
                            break; // Volta ao menu principal
                        }
                        Discipline disciplinaEscolhida = opcionais.get(escolhaDisc - 1);
                        enrolment.enrolOptionaldiscipline(disciplinaEscolhida);
                    }
                    System.out.println("Matrícula realizada!");
                    break; // Volta ao menu principal
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
                        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                                .ofPattern("dd/MM/yyyy");
                        LocalDate dataFinalLocalDate = LocalDate.parse(dataFinal, formatter);
                        secretary.setEnrolPeriod(dataFinalLocalDate);
                        System.out.println("Data final de matrícula definida: " +
                                dataFinalLocalDate.format(formatter));
                    } catch (java.time.format.DateTimeParseException e) {
                        System.out.println("Formato de data inválido! Use o formato dd/MM/yyyy");
                        System.out.println("Exemplo: 31/12/2024");
                    }
                    break;
                case 10:
                    System.out.println("Digite a matéria desejada: ");
                    String disciplina = scanner.nextLine().trim().toLowerCase();

                    try (BufferedReader br = new BufferedReader(new FileReader("AlunoDisciplinas.txt"))) {
                        String linha;
                        boolean encontrou = false;

                        while ((linha = br.readLine()) != null) {
                            String[] partes = linha.split(","); // separa em pedaços

                            if (partes.length >= 3) {
                                String nome = partes[0].split(":")[1].trim();
                                String email = partes[1].split(":")[1].trim();
                                String materia = partes[2].split(":")[1].trim().toLowerCase();
                                String tipo = (partes.length >= 4) ? partes[3].split(":")[1].trim() : "N/A";

                                if (materia.equals(disciplina)) {
                                    System.out.println("Aluno: " + nome + " | Email: " + email + " | Tipo: " + tipo);
                                    encontrou = true;
                                }
                            }
                        }

                        if (!encontrou) {
                            System.out.println("Nenhum aluno encontrado para a disciplina: " + disciplina);
                        }

                    } catch (IOException e) {
                        System.out.println("Erro ao ler o arquivo: " + e.getMessage());
                    }
                    break;
                case 11:
                    Discipline.cancelarDisciplinasComPoucosAlunos();
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
