package br.com.senain116.autoescolan116.controller;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrucao.DadosDetalhamentoAgendamento;
import br.com.senain116.autoescolan116.application.core.domain.enums.Especialidade;
import br.com.senain116.autoescolan116.application.core.usecase.AgendaDeInstrucoes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class InstrucaoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JacksonTester<DadosAgendamento> dadosAgendamentoJson;

    @Autowired
    private JacksonTester<DadosDetalhamentoAgendamento> dadosDetalhamentoAgendamentoJson;

    @MockitoBean //Antiga @MockBean
    private AgendaDeInstrucoes agenda;

    @Test
    @DisplayName("Expectativa: retornar código 400 para informações inválidas")
    @WithMockUser
    void agendarInstrucaoCenario1() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(post("/instrucoes"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Expectativa: retornar código 200 para informações válidas")
    @WithMockUser
    void agendarInstrucaoCenario2() throws Exception {
        LocalDateTime data = LocalDateTime.now().plusHours(1);
        Especialidade especialidade = Especialidade.MOTOS;
        DadosDetalhamentoAgendamento dtoResp = new DadosDetalhamentoAgendamento(
                1L,
                "Aluno Teste",
                "Instrutor Teste",
                especialidade,
                data
        );
        when(agenda.agendar(any())).thenReturn(dtoResp);
        MockHttpServletResponse response = mockMvc
                .perform(post("/instrucoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dadosAgendamentoJson.write(
                                new DadosAgendamento(
                                        1L,
                                        1L,
                                        especialidade,
                                        data
                                )
                        ).getJson())
                )
                .andReturn()
                .getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        String jsonEsperado = dadosDetalhamentoAgendamentoJson.write(dtoResp).getJson();
        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }
}