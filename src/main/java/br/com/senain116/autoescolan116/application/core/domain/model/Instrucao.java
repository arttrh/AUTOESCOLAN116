package br.com.senain116.autoescolan116.application.core.domain.model;

import java.time.LocalDateTime;

public class Instrucao {
    private Long id;
    private Aluno aluno;
    private Instrutor instrutor;
    private LocalDateTime data;

    public Instrucao() {
    }

    public Instrucao(
            Long id,
            Aluno aluno,
            Instrutor instrutor,
            LocalDateTime data) {
        this.id = id;
        this.aluno = aluno;
        this.instrutor = instrutor;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public Instrutor getInstrutor() {
        return instrutor;
    }

    public LocalDateTime getData() {
        return data;
    }
}