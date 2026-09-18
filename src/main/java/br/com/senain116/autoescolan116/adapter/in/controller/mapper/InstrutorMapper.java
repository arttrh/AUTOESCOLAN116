package br.com.senain116.autoescolan116.adapter.in.controller.mapper;

import br.com.senain116.autoescolan116.adapter.in.controller.request.instrutor.DadosCadastroInstrutor;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrutor.DadosDetalhamentoInstrutor;
import br.com.senain116.autoescolan116.adapter.in.controller.response.instrutor.DadosListagemInstrutor;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrutor;
import org.springframework.stereotype.Component;

@Component
public class InstrutorMapper {
    private final EnderecoMapper enderecoMapper;

    public InstrutorMapper(EnderecoMapper enderecoMapper) {
        this.enderecoMapper = enderecoMapper;
    }

    public Instrutor toDomain(DadosCadastroInstrutor dados) {
        return new Instrutor(
                null,
                dados.nome(),
                dados.email(),
                dados.telefone(),
                dados.cnh(),
                dados.especialidade(),
                enderecoMapper.toDomain(dados.endereco()),
                true
        );
    }

    public DadosListagemInstrutor toListDTO(Instrutor instrutor) {
        return new DadosListagemInstrutor(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail(),
                instrutor.getEspecialidade()
        );
    }

    public DadosDetalhamentoInstrutor toDetailsDTO(Instrutor instrutor) {
        return new DadosDetalhamentoInstrutor(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail(),
                instrutor.getTelefone(),
                instrutor.getCnh(),
                instrutor.getEspecialidade(),
                enderecoMapper.toDTO(instrutor.getEndereco()),
                instrutor.isAtivo()
        );
    }
}