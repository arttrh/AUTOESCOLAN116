package br.com.senain116.autoescolan116.application.core.usecase;

import br.com.senain116.autoescolan116.adapter.in.controller.mapper.AlunoMapper;
import br.com.senain116.autoescolan116.adapter.in.controller.mapper.EnderecoMapper;
import br.com.senain116.autoescolan116.adapter.in.controller.request.aluno.DadosAtualizacaoAluno;
import br.com.senain116.autoescolan116.adapter.in.controller.request.aluno.DadosCadastroAluno;
import br.com.senain116.autoescolan116.adapter.in.controller.response.aluno.DadosDetalhamentoAluno;
import br.com.senain116.autoescolan116.adapter.in.controller.response.aluno.DadosListagemAluno;
import br.com.senain116.autoescolan116.application.core.domain.model.Aluno;
import br.com.senain116.autoescolan116.application.port.out.AlunoRepository;
import br.com.senain116.autoescolan116.exception.type.aluno.AlunoNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlunoService {
    private final AlunoRepository repository;
    private final AlunoMapper mapper;
    private final EnderecoMapper enderecoMapper;

    public AlunoService(
            AlunoRepository repository,
            AlunoMapper mapper,
            EnderecoMapper enderecoMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.enderecoMapper = enderecoMapper;
    }

    @Transactional
    public DadosDetalhamentoAluno cadastrarAluno(DadosCadastroAluno dados) {
        Aluno aluno = mapper.toDomain(dados);
        Aluno saved = repository.save(aluno);
        return mapper.toDetailsDTO(saved);
    }

    public Page<DadosListagemAluno> listarAlunos(Pageable paginacao) {
        return repository
                .findAllByAtivoTrue(paginacao)
                .map(mapper::toListDTO);
    }

    public DadosDetalhamentoAluno detalharAluno(Long id) {
        Aluno aluno = repository.findById(id)
                .orElseThrow(() ->
                        new AlunoNotFoundException("ID do aluno informado não existe!"));
        return mapper.toDetailsDTO(aluno);
    }

    @Transactional
    public DadosDetalhamentoAluno atualizarAluno(DadosAtualizacaoAluno dados) {
        Aluno aluno = repository.findById(dados.id())
                .orElseThrow(() ->
                        new AlunoNotFoundException("ID do aluno informado não existe!"));
        aluno.atualizarInformacoes(
                dados.nome(),
                dados.email(),
                dados.telefone(),
                enderecoMapper.toDomain(dados.endereco())
        );
        Aluno saved = repository.save(aluno);
        return mapper.toDetailsDTO(saved);
    }

    @Transactional
    public void excluirAluno(Long id) {
        Aluno aluno = repository.findById(id)
                .orElseThrow(() ->
                        new AlunoNotFoundException("ID do aluno informado não existe!"));
        aluno.excluir();
        repository.save(aluno);
    }
}