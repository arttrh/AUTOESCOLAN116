package br.com.senain116.autoescolan116.application.core.domain.model;

import br.com.senain116.autoescolan116.application.core.domain.enums.Perfil;

public class Usuario {
    private Long id;
    private String login;
    private String senha;
    private boolean ativo = true;
    private Perfil perfil;

    public Usuario() {
    }

    public Usuario(
            Long id,
            String login,
            String senha,
            boolean ativo,
            Perfil perfil) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.ativo = ativo;
        this.perfil = perfil;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void atualizarInformacoes(
            String login,
            Perfil perfil) {
        if (login != null && !login.isBlank()) {
            this.login = login;
        }
        if (perfil != null) {
            this.perfil = perfil;
        }
    }

    public void excluir() {
        this.ativo = false;
    }

    public void atualizarSenha(String senhaNovaCriptografada) {
        this.senha = senhaNovaCriptografada;
    }
}