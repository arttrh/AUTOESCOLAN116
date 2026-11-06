package br.com.senain116.autoescolan116.adapter.out.repository.mapper;

import br.com.senain116.autoescolan116.adapter.out.repository.entity.InstrucaoEntity;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrucao;
import org.springframework.stereotype.Component;

@Component
public class InstrucaoEntityMapper {
    private final InstrutorEntityMapper instrutorEntityMapper;
    private final AlunoEntityMapper alunoEntityMapper;

    public InstrucaoEntityMapper(
            InstrutorEntityMapper instrutorEntityMapper,
            AlunoEntityMapper alunoEntityMapper) {
        this.instrutorEntityMapper = instrutorEntityMapper;
        this.alunoEntityMapper = alunoEntityMapper;
    }

    public Instrucao toDomain(InstrucaoEntity entity) {
        return new Instrucao(
                entity.getId(),
                alunoEntityMapper.toDomain(entity.getAluno()),
                instrutorEntityMapper.toDomain(entity.getInstrutor()),
                entity.getData()
        );
    }

    public InstrucaoEntity toEntity(Instrucao instrucao) {
        return new InstrucaoEntity(
                instrucao.getId(),
                alunoEntityMapper.toEntity(instrucao.getAluno()),
                instrutorEntityMapper.toEntity(instrucao.getInstrutor()),
                instrucao.getData()
        );
    }
}