package br.com.senain116.autoescolan116.application.core.usecase;

import br.com.senain116.autoescolan116.adapter.in.controller.mapper.InstrucaoMapper;
import br.com.senain116.autoescolan116.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrucao.DadosDetalhamentoAgendamento;
import br.com.senain116.autoescolan116.application.core.domain.model.Aluno;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrucao;
import br.com.senain116.autoescolan116.application.core.service.EmailNotificacaoService;
import br.com.senain116.autoescolan116.exception.type.aluno.AlunoNotFoundException;
import br.com.senain116.autoescolan116.exception.type.instrucao.DadosIncompletosException;
import br.com.senain116.autoescolan116.application.port.out.AlunoRepository;
import br.com.senain116.autoescolan116.application.core.validation.interfaces.ValidadorAgendamento;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrutor;
import br.com.senain116.autoescolan116.exception.type.instrutor.InstrutorNotFoundException;
import br.com.senain116.autoescolan116.application.port.out.InstrucaoRepository;
import br.com.senain116.autoescolan116.application.port.out.InstrutorRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeInstrucoes {
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;
    private final InstrucaoRepository repository;
    private final List<ValidadorAgendamento> validadoresAgendamento;
    private final InstrucaoMapper mapper;
    private final EmailNotificacaoService emailNotificacaoService;

    public AgendaDeInstrucoes(
            AlunoRepository alunoRepository,
            InstrutorRepository instrutorRepository,
            InstrucaoRepository repository,
            List<ValidadorAgendamento> validadoresAgendamento,
            InstrucaoMapper mapper,
            EmailNotificacaoService emailNotificacaoService) {
        this.alunoRepository = alunoRepository;
        this.instrutorRepository = instrutorRepository;
        this.repository = repository;
        this.validadoresAgendamento = validadoresAgendamento;
        this.mapper = mapper;
        this.emailNotificacaoService = emailNotificacaoService;
    }

    @Transactional
    public DadosDetalhamentoAgendamento agendar(DadosAgendamento dados) {
        if (!alunoRepository.existsByIdAndAtivoTrue(dados.idAluno())) {
            throw new AlunoNotFoundException("ID do aluno informado não existe!");
        }
        if (dados.idInstrutor() != null && !instrutorRepository.existsByIdAndAtivoTrue(dados.idInstrutor())) {
            throw new InstrutorNotFoundException("ID do instrutor informado não existe!");
        }


        //Validações
        validadoresAgendamento.forEach(v -> v.validar(dados));

        Aluno aluno = alunoRepository.findById(dados.idAluno()).orElseThrow(() -> new AlunoNotFoundException("ID do aluno informado não existe!"));
        Instrutor instrutor = escolherInstrutor(dados);
        Instrucao instrucao = new Instrucao(
                null,
                aluno,
                instrutor,
                dados.data()
        );
        Instrucao saved = repository.save(instrucao);
        emailNotificacaoService.enviarNotificacaoInstrucao(saved, "agendada");
        return new DadosDetalhamentoAgendamento(saved);
    }

    @Transactional
    public void excluirAgendamento(Long id){
        Instrucao agenda = repository.findById(id)
                .orElseThrow(() -> new DadosIncompletosException("ID do agendamento não encontrado: " + id));
        repository.delete(agenda);
    }

    @Transactional
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

    public Page listar(Pageable paginacao){
        return repository
                .findAll(paginacao)
                .map(mapper::toListDTO);
    }

    public DadosDetalhamentoAgendamento detalhar(Long id){
        Instrucao instrucao = repository.findById(id)
                .orElseThrow(() -> new DadosIncompletosException("ID do agendamento não encontrado: " + id));
        return mapper.toDetails(instrucao);
    }
}