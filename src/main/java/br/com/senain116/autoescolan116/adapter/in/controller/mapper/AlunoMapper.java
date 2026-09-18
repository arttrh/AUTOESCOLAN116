package br.com.senain116.autoescolan116.adapter.in.controller.mapper;

import br.com.senain116.autoescolan116.adapter.in.controller.request.aluno.DadosCadastroAluno;
import br.com.senain116.autoescolan116.adapter.in.controller.response.aluno.DadosDetalhamentoAluno;
import br.com.senain116.autoescolan116.adapter.in.controller.response.aluno.DadosListagemAluno;
import br.com.senain116.autoescolan116.application.core.domain.model.Aluno;
import org.springframework.stereotype.Component;

@Component
public class AlunoMapper {
    private final EnderecoMapper enderecoMapper;

    public AlunoMapper(EnderecoMapper enderecoMapper) {
        this.enderecoMapper = enderecoMapper;
    }

    public Aluno toDomain(DadosCadastroAluno dados) {
        return new Aluno(
                null,
                dados.nome(),
                dados.email(),
                dados.telefone(),
                dados.cpf(),
                enderecoMapper.toDomain(dados.endereco()),
                true
        );
    }

    public DadosListagemAluno toListDTO(Aluno aluno) {
        return new DadosListagemAluno(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail()
        );
    }

    public DadosDetalhamentoAluno toDetailsDTO(Aluno aluno) {
        return new DadosDetalhamentoAluno(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getCpf(),
                enderecoMapper.toDTO(aluno.getEndereco()),
                aluno.isAtivo()
        );
    }
}