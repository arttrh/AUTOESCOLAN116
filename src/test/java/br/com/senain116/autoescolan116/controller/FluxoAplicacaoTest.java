package br.com.senain116.autoescolan116.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;
import br.com.senain116.autoescolan116.adapter.in.controller.request.usuario.DadosCadastroUsuario;
import br.com.senain116.autoescolan116.application.core.domain.enums.Perfil;
import br.com.senain116.autoescolan116.application.core.usecase.UsuarioService;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import java.time.LocalDate;
import java.util.Map;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FluxoAplicacaoTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired UsuarioService usuarios;

    private JsonNode enviar(MockHttpServletRequestBuilder request, Object data, String token, int status) throws Exception {
        if (data != null) request.contentType(MediaType.APPLICATION_JSON).content(json.writeValueAsString(data));
        if (token != null) request.header("Authorization", "Bearer " + token);
        String body = mvc.perform(request).andExpect(status().is(status)).andReturn().getResponse().getContentAsString();
        return body.isBlank() ? json.readTree("null") : json.readTree(body);
    }

    private JsonNode acesso(String login, Perfil perfil) throws Exception {
        usuarios.cadastrarUsuario(new DadosCadastroUsuario(login, "SenhaDeTeste123!", perfil));
        JsonNode response = enviar(post("/login"), Map.of("login", login, "senha", "SenhaDeTeste123!"), null, 200);
        assertThat(response.get("perfil").asText()).isEqualTo(perfil.name());
        assertThat(response.has("senha")).isFalse();
        var expiracao = com.auth0.jwt.JWT.decode(response.get("tokenJWT").asText()).getExpiresAtAsInstant();
        assertThat(expiracao).isBetween(java.time.Instant.now().plusSeconds(1740), java.time.Instant.now().plusSeconds(1810));
        return response;
    }

    @Test
    void cadastroPublicoCriaSomenteUsuarioComum() throws Exception {
        var dados = Map.of(
                "login", "novo-publico",
                "senha", "SenhaPublica123!",
                "perfil", "ADMIN"
        );
        JsonNode cadastro = enviar(post("/usuarios/cadastro"), dados, null, 201);
        assertThat(cadastro.get("perfil").asText()).isEqualTo("USER");
        assertThat(cadastro.has("senha")).isFalse();

        enviar(post("/usuarios/cadastro"), Map.of(
                "login", "outro-publico",
                "senha", "curta"
        ), null, 400);

        JsonNode login = enviar(post("/login"), Map.of(
                "login", "novo-publico",
                "senha", "SenhaPublica123!"
        ), null, 200);
        assertThat(login.get("perfil").asText()).isEqualTo("USER");
        enviar(get("/usuarios"), null, login.get("tokenJWT").asText(), 403);
        enviar(post("/usuarios/cadastro"), dados, null, 409);
    }

    @Test
    void protegerPerfisSenhasEContasInativas() throws Exception {
        enviar(get("/alunos"), null, null, 401);
        enviar(get("/alunos"), null, "token-invalido", 401);
        enviar(post("/login"), Map.of("login", "inexistente", "senha", "SenhaErrada123!"), null, 401);
        JsonNode admin = acesso("teste-admin", Perfil.ADMIN);
        JsonNode equipe = acesso("teste-equipe", Perfil.USER);
        String token = equipe.get("tokenJWT").asText();
        enviar(get("/usuarios"), null, token, 403);
        enviar(patch("/usuarios"), Map.of("id", admin.get("id").asLong(), "senhaAntiga", "SenhaDeTeste123!", "senhaNova", "OutraSenha123!"), token, 403);
        enviar(patch("/usuarios"), Map.of("id", equipe.get("id").asLong(), "senhaAntiga", "SenhaDeTeste123!", "senhaNova", "OutraSenha123!"), token, 200);
        enviar(post("/login"), Map.of("login", "teste-equipe", "senha", "SenhaDeTeste123!"), null, 401);
        enviar(post("/login"), Map.of("login", "teste-equipe", "senha", "OutraSenha123!"), null, 200);
        enviar(delete("/usuarios/" + equipe.get("id").asLong()), null, admin.get("tokenJWT").asText(), 204);
        enviar(get("/alunos"), null, token, 401);
        enviar(post("/login"), Map.of("login", "teste-equipe", "senha", "OutraSenha123!"), null, 401);
    }

    @Test
    void cadastrarEditarAgendarEConsultarComValidacoes() throws Exception {
        String token = acesso("teste-agenda", Perfil.ADMIN).get("tokenJWT").asText();
        var endereco = Map.of("logradouro", "Rua Exemplo", "numero", "1", "bairro", "Centro", "cidade", "São Paulo", "uf", "SP", "cep", "01001000");
        var aluno = Map.of("nome", "Aluno de teste", "email", "aluno@example.com", "telefone", "(11) 99999-0000", "cpf", "12345678901", "endereco", endereco);
        enviar(post("/alunos"), Map.of("nome", "Sem endereço", "email", "invalido@example.com", "telefone", "(11) 99999-0000", "cpf", "12345678902"), token, 400);
        long alunoId = enviar(post("/alunos"), aluno, token, 201).get("id").asLong();
        enviar(put("/alunos"), Map.of("id", alunoId, "nome", "Aluno atualizado"), token, 200);
        assertThat(enviar(get("/alunos/" + alunoId), null, token, 200).get("nome").asText()).isEqualTo("Aluno atualizado");
        long instrutorId = enviar(post("/instrutores"), Map.of("nome", "Instrutor de teste", "email", "instrutor@example.com", "telefone", "(11) 99999-0001", "cnh", "12345678903", "especialidade", "CARROS", "endereco", endereco), token, 201).get("id").asLong();
        LocalDate dia = LocalDate.now().plusDays(2);
        if (dia.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) dia = dia.plusDays(1);
        String data = dia.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        enviar(post("/instrucoes"), Map.of("idAluno", alunoId, "idInstrutor", instrutorId, "data", data + " - 05:00"), token, 422);
        enviar(post("/instrucoes"), Map.of("idAluno", alunoId, "idInstrutor", instrutorId, "data", data + " - 10:30"), token, 422);
        enviar(post("/instrucoes"), Map.of("idAluno", alunoId, "idInstrutor", instrutorId, "data", data + " - 10:00"), token, 200);
        enviar(post("/instrucoes"), Map.of("idAluno", alunoId, "idInstrutor", instrutorId, "data", data + " - 11:00"), token, 422);
        JsonNode agenda = enviar(get("/instrucoes?inicio=" + dia + "&fim=" + dia.plusDays(1)), null, token, 200);
        assertThat(agenda.size()).isEqualTo(1);
        assertThat(agenda.get(0).get("aluno").asText()).isEqualTo("Aluno atualizado");
        enviar(get("/instrucoes?inicio=" + dia + "&fim=" + dia.plusDays(32)), null, token, 422);
        enviar(get("/instrucoes?inicio=data-invalida&fim=" + dia), null, token, 400);
        enviar(delete("/alunos/" + alunoId), null, token, 204);
        assertThat(enviar(get("/alunos"), null, token, 200).get("totalElements").asInt()).isZero();
        enviar(post("/alunos"), aluno, token, 409);
    }

}
