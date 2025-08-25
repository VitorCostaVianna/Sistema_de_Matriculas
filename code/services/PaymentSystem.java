package services;

import users.Student;

public class PaymentSystem {

    private static PaymentSystem instancia;

    private PaymentSystem() {
    }

    public static PaymentSystem getInstancia() {
        if (instancia == null) {
            instancia = new PaymentSystem();
        }
        return instancia;
    }

    public void gerarCobranca(Student aluno) {
        System.out.println("Gerando cobrança para o aluno: " + aluno.getName());
        // TODO - Criar arquivo da cobrança
    }
}
