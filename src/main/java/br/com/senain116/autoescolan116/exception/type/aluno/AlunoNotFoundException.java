package br.com.senain116.autoescolan116.exception.type.aluno;

public class AlunoNotFoundException extends RuntimeException {
    public AlunoNotFoundException(String message) {
        super(message);
    }
}