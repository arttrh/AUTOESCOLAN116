package br.com.senain116.autoescolan116.adapter.out.repository.mapper;

import br.com.senain116.autoescolan116.adapter.out.repository.entity.InstrutorEntity;
import br.com.senain116.autoescolan116.application.core.domain.model.Instrutor;
import org.springframework.stereotype.Component;

@Component
public class InstrutorEntityMapper {
    public Instrutor toDomain(InstrutorEntity entity) {
        return new Instrutor(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getTelefone(),
                entity.getCnh(),
                entity.getEspecialidade(),
                entity.getEndereco(),
                entity.isAtivo()
        );
    }

    public InstrutorEntity toEntity(Instrutor instrutor) {
        return new InstrutorEntity(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail(),
                instrutor.getTelefone(),
                instrutor.getCnh(),
                instrutor.getEspecialidade(),
                instrutor.getEndereco(),
                instrutor.isAtivo()
        );
    }
}