package br.com.senain116.autoescolan116.config;
import br.com.senain116.autoescolan116.adapter.in.controller.request.usuario.DadosCadastroUsuario;
import br.com.senain116.autoescolan116.application.core.domain.enums.Perfil;
import br.com.senain116.autoescolan116.application.core.usecase.UsuarioService;
import br.com.senain116.autoescolan116.application.port.out.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Component
public class AdministradorInicial implements CommandLineRunner {
    private final UsuarioService usuarios;
    private final UsuarioRepository repository;
    private final String login, password;
    public AdministradorInicial(UsuarioService usuarios, UsuarioRepository repository,
        @Value("${ADMIN_LOGIN:}") String login, @Value("${ADMIN_PASSWORD:}") String password) {
        this.usuarios = usuarios;
        this.repository = repository;
        this.login = login;
        this.password = password;
    }
    public void run(String... args) {
        if (!login.isBlank() && !password.isBlank() && repository.findByLogin(login) == null)
            usuarios.cadastrarUsuario(new DadosCadastroUsuario(login, password, Perfil.ADMIN));
    }
}
