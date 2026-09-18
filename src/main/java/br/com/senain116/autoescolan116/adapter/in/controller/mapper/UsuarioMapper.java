package br.com.senain116.autoescolan116.adapter.in.controller.mapper;

import br.com.senain116.autoescolan116.adapter.in.controller.request.usuario.DadosCadastroUsuario;
import br.com.senain116.autoescolan116.adapter.in.controller.response.usuario.DadosDetalhamentoUsuario;
import br.com.senain116.autoescolan116.adapter.in.controller.response.usuario.DadosListagemUsuario;
import br.com.senain116.autoescolan116.application.core.domain.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public Usuario toDomain(DadosCadastroUsuario dados) {
        return new Usuario(
                null,
                dados.login(),
                dados.senha(),
                true,
                dados.perfil()
        );
    }

    public DadosListagemUsuario toListDTO(Usuario usuario) {
        return new DadosListagemUsuario(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getPerfil()
        );
    }

    public DadosDetalhamentoUsuario toDetailsDTO(Usuario usuario) {
        return new DadosDetalhamentoUsuario(
                usuario.getId(),
                usuario.getLogin(),
                usuario.isAtivo(),
                usuario.getPerfil()
        );
    }
}