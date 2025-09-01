package services;

import users.Student;
import subject.Discipline;
import java.io.*;
import java.util.*;

public class PaymentSystem {

    private static PaymentSystem instancia;

    private PaymentSystem() {}

    public static PaymentSystem getInstancia() {
        if (instancia == null) {
            instancia = new PaymentSystem();
        }
        return instancia;
    }

    public void gerarCobranca(Student aluno, Discipline disciplina) {
        double valor = calcularValor(disciplina);
        atualizarArquivoCobranca(aluno, valor);
        System.out.println("Cobrança adicionada: " + valor + " para " + aluno.getName());
    }

    public void removerCobranca(Student aluno, Discipline disciplina) {
        double valor = -calcularValor(disciplina);
        atualizarArquivoCobranca(aluno, valor);
        System.out.println("Cobrança removida: " + (-valor) + " de " + aluno.getName());
    }

    private void atualizarArquivoCobranca(Student aluno, double valorDelta) {
        File file = new File("Cobranca.txt");
        List<String> linhas = new ArrayList<>();
        boolean alunoEncontrado = false;

        try {
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("email: " + aluno.getEmail())) {
                        String[] partes = line.split(",");
                        String valorStr = partes[2].split(":")[1].trim().replace("R$", "").trim();
                        double valorAtual = Double.parseDouble(valorStr);

                        double valorNovo = Math.max(0, valorAtual + valorDelta);

                        line = "nome: " + aluno.getName()
                             + ", email: " + aluno.getEmail()
                             + ", valor: R$ " + valorNovo;
                        alunoEncontrado = true;
                    }
                    linhas.add(line);
                }
                reader.close();
            }

            if (!alunoEncontrado && valorDelta > 0) {
                linhas.add("nome: " + aluno.getName()
                        + ", email: " + aluno.getEmail()
                        + ", valor: R$ " + valorDelta);
            }

            FileWriter writer = new FileWriter(file, false);
            for (String l : linhas) {
                writer.write(l + System.lineSeparator());
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double calcularValor(Discipline disciplina) {
        return disciplina.isRequired() ? 500.0 : 300.0;
    }
}
