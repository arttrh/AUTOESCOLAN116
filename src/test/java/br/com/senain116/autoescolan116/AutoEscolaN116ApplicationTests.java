package br.com.senain116.autoescolan116;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@org.springframework.test.context.ActiveProfiles("test")
class AutoEscolaN116ApplicationTests {

    @org.springframework.beans.factory.annotation.Autowired
    private br.com.senain116.autoescolan116.adapter.out.repository.persistence.UsuarioJpaRepository usuarios;

    @Test
    void naoManterAdministradorComSenhaPublica() {
        org.assertj.core.api.Assertions.assertThat(usuarios.findByLogin("admin")).isNull();
    }

}
