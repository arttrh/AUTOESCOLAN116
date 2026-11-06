package br.com.senain116.autoescolan116.adapter.out.repository.mapper;

import br.com.senain116.autoescolan116.adapter.out.repository.entity.AlunoEntity;
import br.com.senain116.autoescolan116.application.core.domain.model.Aluno;
import org.springframework.stereotype.Component;

@Component
public class AlunoEntityMapper {
    public Aluno toDomain(AlunoEntity entity) {
        return new Aluno(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getCpf(),
                entity.getEndereco(),
                entity.isAtivo()
        );
    }

    public AlunoEntity toEntity(Aluno aluno) {
        return new AlunoEntity(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getCpf(),
                aluno.getEndereco(),
                aluno.isAtivo()
        );
    }
}