package br.com.senain116.autoescolan116.application.core.domain.model;

import br.com.senain116.autoescolan116.application.core.domain.vo.Endereco;

public class Aluno {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private Endereco endereco;
    private boolean ativo = true;

    public Aluno() {
    }

    public Aluno(
            Long id,
            String nome,
            String email,
            String telefone,
            String cpf,
            Endereco endereco,
            boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.endereco = endereco;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void atualizarInformacoes(
            String nome,
            String email,
            String telefone,
            Endereco endereco) {
        if (nome != null && !nome.isBlank()) {
            this.nome = nome;
        }
        if (email != null && !email.isBlank()) {
            this.email = email;
        }
        if (telefone != null && !telefone.isBlank()) {
            this.telefone = telefone;
        }
        if (endereco != null) {
            this.endereco.atualizarInformacoes(
                    endereco.getLogradouro(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getBairro(),
                    endereco.getCidade(),
                    endereco.getUf(),
                    endereco.getCep()
            );
        }
    }

    public void excluir() {
        this.ativo = false;
    }
}