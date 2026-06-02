package br.com.senain116.autoescolan116.application.core.domain.vo;

import jakarta.persistence.Embeddable;

@Embeddable
public class Endereco {
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;

    public Endereco() {
    }

    public Endereco(
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String uf,
            String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getUf() {
        return uf;
    }

    public String getCep() {
        return cep;
    }

    public void atualizarInformacoes(
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String uf,
            String cep) {
        if (logradouro != null && !logradouro.isBlank()) {
            this.logradouro = logradouro;
        }
        if (numero != null) {
            this.numero = numero;
        }
        if (complemento != null) {
            this.complemento = complemento;
        }
        if (bairro != null && !bairro.isBlank()) {
            this.bairro = bairro;
        }
        if (cidade != null && !cidade.isBlank()) {
            this.cidade = cidade;
        }
        if (uf != null && !uf.isBlank()) {
            this.uf = uf;
        }
        if (cep != null && !cep.isBlank()) {
            this.cep = cep;
        }
    }
}