package br.com.senain116.autoescolan116.application.core.usecase;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrucao.DadosDetalhamentoAgendamento;
import br.com.senain116.autoescolan116.application.core.domain.model.Aluno;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrucao;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrutor;
import br.com.senain116.autoescolan116.application.core.service.EmailNotificacaoService;
import br.com.senain116.autoescolan116.application.core.validation.interfaces.ValidadorAgendamento;
import br.com.senain116.autoescolan116.application.port.out.AlunoRepository;
import br.com.senain116.autoescolan116.application.port.out.InstrucaoRepository;
import br.com.senain116.autoescolan116.application.port.out.InstrutorRepository;
import br.com.senain116.autoescolan116.exception.type.aluno.AlunoNotFoundException;
import br.com.senain116.autoescolan116.exception.type.instrucao.DadosIncompletosException;
import br.com.senain116.autoescolan116.exception.type.instrutor.InstrutorNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AgendaDeInstrucoes {
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;
    private final InstrucaoRepository repository;
    private final List<ValidadorAgendamento> validadoresAgendamento;
    private final EmailNotificacaoService emailNotificacaoService;
    //private static final Logger log = LoggerFactory.getLogger(AgendaDeInstrucoes.class);

    public AgendaDeInstrucoes(
            AlunoRepository alunoRepository,
            InstrutorRepository instrutorRepository,
            InstrucaoRepository repository,
            List<ValidadorAgendamento> validadoresAgendamento,
            EmailNotificacaoService emailNotificacaoService) {
        this.alunoRepository = alunoRepository;
        this.instrutorRepository = instrutorRepository;
        this.repository = repository;
        this.validadoresAgendamento = validadoresAgendamento;
        this.emailNotificacaoService = emailNotificacaoService;
    }

    @Transactional
    public DadosDetalhamentoAgendamento agendarInstrucao(DadosAgendamento dados) {
        log.info("Agendamento iniciado para aluno: {}, Intrutor: {}, Data: {}", dados.idAluno(), dados.idInstrutor(), dados.data());
        if (!alunoRepository.existsById(dados.idAluno())) {
            log.warn("Tentativa de agendamento para aluno inexistente Aluno: {}", dados.idAluno());
            //log.warn("Aluno com ID {} não encontrado para agendamento" + dados.idAluno());
            throw new AlunoNotFoundException("ID do aluno informado não existe!");
        }
        if (dados.idInstrutor() != null && !instrutorRepository.existsById(dados.idInstrutor())) {
            log.warn("Tentativa de agendamento para instrutor inexistente Instrutor: {}", dados.idInstrutor());
            throw new InstrutorNotFoundException("ID do instrutor informado não existe!");
        }

        //Validações
        validadoresAgendamento.forEach(v -> v.validar(dados));
        log.debug("Validações concluídas para agendamento do aluno: {}, Intrutor: {}, Data: {}", dados.idAluno(), dados.idInstrutor(), dados.data());

        Aluno aluno = alunoRepository.getReferenceById(dados.idAluno());
        Instrutor instrutor = escolherInstrutor(dados);
        Instrucao instrucao = new Instrucao(
                null,
                aluno,
                instrutor,
                dados.data()
        );
        Instrucao saved = repository.save(instrucao);
        emailNotificacaoService.enviarNotificacaoInstrucao(saved, "agendada");
        log.info("Agendamento concluído para aluno: {}, Intrutor: {}, Data: {}", dados.idAluno(), dados.idInstrutor(), dados.data());
        return new DadosDetalhamentoAgendamento(saved);
    }

    private Instrutor escolherInstrutor(DadosAgendamento dados) {
        if (dados.idInstrutor() != null) {
            return instrutorRepository.getReferenceById(dados.idInstrutor());
        }
        if (dados.especialidade() == null) {
            throw new DadosIncompletosException("Especialidade é obrigatória quando o instrutor não for informado!");
        }
        return instrutorRepository.escolherInstrutorAleatorioDisponivel(
                dados.especialidade(),
                dados.data()
        );
    }
}